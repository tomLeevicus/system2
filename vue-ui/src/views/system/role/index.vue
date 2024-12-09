<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="角色名称" prop="roleName">
        <el-input
          v-model="queryParams.roleName"
          placeholder="请输入角色名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="权限字符" prop="roleKey">
        <el-input
          v-model="queryParams.roleKey"
          placeholder="请输入权限字符"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="角色状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区域 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:role:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:role:delete']"
        >删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格区域 -->
    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="角色编号" prop="roleId" width="120" />
      <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="权限字符" prop="roleKey" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="显示顺序" prop="roleSort" width="100" />
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="'0'"
            :inactive-value="'1'"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:role:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:role:delete']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区域 -->
    <pagination
      v-if="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改角色对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="roleFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="权限字符" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="请输入权限字符" />
        </el-form-item>
        <el-form-item label="角色顺序" prop="roleSort">
          <el-input-number v-model="form.roleSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree
            ref="menuRef"
            :data="menuOptions"
            show-checkbox
            node-key="id"
            empty-text="加载中，请稍候"
            :props="{ label: 'label', children: 'children' }"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listRole, getRole, addRole, updateRole, deleteRole, changeRoleStatus } from '@/api/system/role'
import { listMenu, getMenuTree } from '@/api/system/menu'
import type { RoleForm, RoleInfo } from '@/api/system/role'

// 遮罩层
const loading = ref(false)
// 选中数组
const ids = ref<number[]>([])
// 非单个禁用
const single = ref(true)
// 非多个禁用
const multiple = ref(true)
// 总条数
const total = ref(0)
// 角色表格数据
const roleList = ref<RoleInfo[]>([])
// 弹出层标题
const title = ref('')
// 是否显示弹出层
const open = ref(false)
// 菜单列表
const menuOptions = ref([])
// 菜单树选择引用
const menuRef = ref()

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: undefined,
  roleKey: undefined,
  status: undefined
})

// 表单参数
const form = reactive<RoleForm>({
  roleId: undefined,
  roleName: '',
  roleKey: '',
  roleSort: 0,
  status: '0',
  remark: '',
  menuIds: []
})

// 表单校验
const rules = {
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' }
  ],
  roleKey: [
    { required: true, message: '权限字符不能为空', trigger: 'blur' }
  ],
  roleSort: [
    { required: true, message: '角色顺序不能为空', trigger: 'blur' }
  ]
}

// 表单引用
const roleFormRef = ref()
const queryRef = ref()

/** 查询角色列表 */
async function getList() {
  loading.value = true
  try {
    const res = await listRole(queryParams)
    roleList.value = res.data.rows
    total.value = res.data.total
  } catch (error) {
    console.error('获取角色列表失败:', error)
  } finally {
    loading.value = false
  }
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.roleId = undefined
  form.roleName = ''
  form.roleKey = ''
  form.roleSort = 0
  form.status = '0'
  form.remark = ''
  form.menuIds = []
  roleFormRef.value?.resetFields()
  menuRef.value?.setCheckedKeys([])
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryRef.value?.resetFields()
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection: RoleInfo[]) {
  ids.value = selection.map(item => item.roleId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
async function handleAdd() {
  reset()
  await getMenuTreeSelect()
  open.value = true
  title.value = '添加角色'
}

/** 修改按钮操作 */
async function handleUpdate(row: RoleInfo) {
  reset()
  const roleId = row.roleId || ids.value[0]
  const [roleRes, menuRes] = await Promise.all([
    getRole(roleId),
    getMenuTreeSelect()
  ])
  form.roleId = roleRes.data.roleId
  form.roleName = roleRes.data.roleName
  form.roleKey = roleRes.data.roleKey
  form.roleSort = roleRes.data.roleSort
  form.status = roleRes.data.status
  form.remark = roleRes.data.remark
  menuRef.value.setCheckedKeys(roleRes.data.menuIds)
  open.value = true
  title.value = '修改角色'
}

/** 提交按钮 */
function submitForm() {
  roleFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.menuIds = menuRef.value.getCheckedKeys()
      try {
        if (form.roleId) {
          await updateRole(form)
          ElMessage.success('修改成功')
        } else {
          await addRole(form)
          ElMessage.success('新增成功')
        }
        open.value = false
        getList()
      } catch (error: any) {
        ElMessage.error(error.message || '操作失败')
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row?: RoleInfo) {
  const roleIds = row?.roleId || ids.value
  ElMessageBox.confirm('是否确认删除所选角色?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(roleIds)
      getList()
      ElMessage.success('删除成功')
    } catch (error: any) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

/** 角色状态修改 */
async function handleStatusChange(row: RoleInfo) {
  const text = row.status === '0' ? '启用' : '停用'
  try {
    await changeRoleStatus(row.roleId, row.status)
    ElMessage.success(`${text}成功`)
  } catch (error: any) {
    ElMessage.error(error.message || `${text}失败`)
    row.status = row.status === '0' ? '1' : '0'
  }
}

/** 查询菜单树结构 */
async function getMenuTreeSelect() {
  try {
    const res = await getMenuTree()
    menuOptions.value = res.data
  } catch (error) {
    console.error('获取菜单树失败:', error)
  }
}

onMounted(() => {
  getList()
})
</script> 