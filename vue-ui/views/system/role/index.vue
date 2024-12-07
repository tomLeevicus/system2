<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <div class="right-menu">
            <el-button type="primary" @click="handleAdd">新增角色</el-button>
          </div>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
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
            <el-option
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="roleList"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="角色编号" prop="roleId" width="120" />
        <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" />
        <el-table-column label="权限字符" prop="roleKey" :show-overflow-tooltip="true" />
        <el-table-column label="显示顺序" prop="roleSort" width="100" />
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="250">
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              type="primary"
              link
              @click="handleDataScope(scope.row)"
            >数据权限</el-button>
            <el-button
              type="primary"
              link
              @click="handleDelete(scope.row)"
              v-if="scope.row.roleId !== 1"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改角色对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="roleForm" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角��名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="权限字符" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="请输入权限字符" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="roleSort">
          <el-input-number v-model="form.roleSort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
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

    <!-- 分配数据权限对话框 -->
    <el-dialog :title="title" v-model="openDataScope" width="600px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" :disabled="true" />
        </el-form-item>
        <el-form-item label="权限字符">
          <el-input v-model="form.roleKey" :disabled="true" />
        </el-form-item>
        <el-form-item label="权限范围">
          <el-select v-model="form.dataScope" @change="dataScopeSelectChange">
            <el-option
              v-for="item in dataScopeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限" v-show="form.dataScope == 2">
          <el-tree
            ref="deptRef"
            :data="deptOptions"
            show-checkbox
            node-key="id"
            empty-text="加载中，请稍候"
            :props="{ label: 'label', children: 'children' }"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitDataScope">确 定</el-button>
          <el-button @click="cancelDataScope">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listRole, getRole, addRole, updateRole, delRole, changeRoleStatus, dataScope } from '@/api/system/role'
import { treeselect as menuTreeselect, roleMenuTreeselect } from '@/api/system/menu'
import { treeselect as deptTreeselect, roleDeptTreeselect } from '@/api/system/dept'

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 角色列表
const roleList = ref([])
// 弹出层标题
const title = ref('')
// 是否显示弹出层
const open = ref(false)
// 是否显示数据权限弹出层
const openDataScope = ref(false)
// 菜单树选项
const menuOptions = ref([])
// 部门树选项
const deptOptions = ref([])
// 状态数据字典
const statusOptions = [
  { value: '0', label: '正常' },
  { value: '1', label: '停用' }
]
// 数据范围选项
const dataScopeOptions = [
  { value: '1', label: '全部数据权限' },
  { value: '2', label: '自定数据权限' },
  { value: '3', label: '本部门数据权限' },
  { value: '4', label: '本部门及以下数据权限' },
  { value: '5', label: '仅本人数据权限' }
]

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: undefined,
  roleKey: undefined,
  status: undefined
})

// 表单参数
const form = reactive({
  roleId: undefined,
  roleName: undefined,
  roleKey: undefined,
  roleSort: 0,
  status: '0',
  menuIds: [],
  deptIds: [],
  dataScope: '1',
  remark: undefined
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
    { required: true, message: '显示顺序不能为空', trigger: 'blur' }
  ]
}

/** 查询角色列表 */
function getList() {
  loading.value = true
  listRole(queryParams).then(response => {
    roleList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.roleName = undefined
  queryParams.roleKey = undefined
  queryParams.status = undefined
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  getMenuTreeselect()
  open.value = true
  title.value = '添加角色'
}

/** 修改按钮操作 */
function handleUpdate(row: any) {
  reset()
  const roleId = row.roleId
  getRole(roleId).then(response => {
    form.value = response.data
    getRoleMenuTreeselect(roleId)
    open.value = true
    title.value = '修改角色'
  })
}

/** 分配数据权限操作 */
function handleDataScope(row: any) {
  reset()
  const roleId = row.roleId
  getRole(roleId).then(response => {
    form.value = response.data
    getRoleDeptTreeselect(roleId)
    openDataScope.value = true
    title.value = '分配数据权限'
  })
}

/** 提交按钮 */
function submitForm() {
  roleForm.value.validate((valid: boolean) => {
    if (valid) {
      form.menuIds = getMenuAllCheckedKeys()
      if (form.roleId) {
        updateRole(form).then(response => {
          ElMessage.success('修改成功')
          open.value = false
          getList()
        })
      } else {
        addRole(form).then(response => {
          ElMessage.success('新增成功')
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 提交按钮（数据权限） */
function submitDataScope() {
  if (form.dataScope == '2') {
    form.deptIds = getDeptAllCheckedKeys()
  }
  dataScope(form).then(response => {
    ElMessage.success('修改成功')
    openDataScope.value = false
    getList()
  })
}

/** 删除按钮操作 */
function handleDelete(row: any) {
  const roleIds = [row.roleId]
  ElMessageBox.confirm('是否确认删除角色编号为"' + roleIds + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delRole(roleIds)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  })
}

/** 角色状态修改 */
function handleStatusChange(row: any) {
  const text = row.status === '0' ? '启用' : '停用'
  ElMessageBox.confirm('确认要"' + text + '""' + row.roleName + '"角色吗?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return changeRoleStatus(row.roleId, row.status)
  }).then(() => {
    ElMessage.success(text + '成功')
  }).catch(() => {
    row.status = row.status === '0' ? '1' : '0'
  })
}

/** 选择角色权限范围触发 */
function dataScopeSelectChange(value: string) {
  if (value !== '2') {
    deptRef.value.setCheckedKeys([])
  }
}

/** 获取菜单树选择项 */
function getMenuTreeselect() {
  menuTreeselect().then(response => {
    menuOptions.value = response.data
  })
}

/** 获取部门树选择项 */
function getDeptTreeselect() {
  deptTreeselect().then(response => {
    deptOptions.value = response.data
  })
}

/** 根据角色ID获取菜单树选择项 */
function getRoleMenuTreeselect(roleId: number) {
  roleMenuTreeselect(roleId).then(response => {
    menuOptions.value = response.menus
    menuRef.value.setCheckedKeys(response.checkedKeys)
  })
}

/** 根据角色ID获取部门树选择项 */
function getRoleDeptTreeselect(roleId: number) {
  getDeptTreeselect()
  roleDeptTreeselect(roleId).then(response => {
    deptRef.value.setCheckedKeys(response.checkedKeys)
  })
}

/** 获取所有选中菜单节点 */
function getMenuAllCheckedKeys() {
  // 目前被选中的菜单节点
  const checkedKeys = menuRef.value.getCheckedKeys()
  // 半选中的菜单节点
  const halfCheckedKeys = menuRef.value.getHalfCheckedKeys()
  return checkedKeys.concat(halfCheckedKeys)
}

/** 获取所有选中部门节点 */
function getDeptAllCheckedKeys() {
  // 目前被选中的部门节点
  const checkedKeys = deptRef.value.getCheckedKeys()
  // 半选中的部门节点
  const halfCheckedKeys = deptRef.value.getHalfCheckedKeys()
  return checkedKeys.concat(halfCheckedKeys)
}

/** 表单重置 */
function reset() {
  form.roleId = undefined
  form.roleName = undefined
  form.roleKey = undefined
  form.roleSort = 0
  form.status = '0'
  form.menuIds = []
  form.deptIds = []
  form.dataScope = '1'
  form.remark = undefined
  roleForm.value?.resetFields()
  menuRef.value?.setCheckedKeys([])
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 取消按钮（数据权限） */
function cancelDataScope() {
  openDataScope.value = false
  reset()
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 