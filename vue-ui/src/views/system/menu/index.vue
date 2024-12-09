<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="菜单名称" prop="menuName">
        <el-input
          v-model="queryParams.menuName"
          placeholder="请输入菜单名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="菜单状态" clearable>
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
          v-hasPermi="['system:menu:add']"
        >新增</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格区域 -->
    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="menuId"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" width="160" />
      <el-table-column prop="icon" label="图标" align="center" width="100">
        <template #default="scope">
          <el-icon v-if="scope.row.icon">
            <component :is="scope.row.icon" />
          </el-icon>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="60" align="center" />
      <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true" />
      <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">
            {{ scope.row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:menu:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:menu:add']"
          >新增</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:menu:delete']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" v-model="open" width="680px" append-to-body>
      <el-form ref="menuFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="menuOptions"
            :props="{ value: 'menuId', label: 'menuName', children: 'children' }"
            value-key="menuId"
            placeholder="选择上级菜单"
            check-strictly
            :render-after-expand="false"
          />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="form.menuType">
            <el-radio label="M">目录</el-radio>
            <el-radio label="C">菜单</el-radio>
            <el-radio label="F">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="图标" prop="icon" v-if="form.menuType !== 'F'">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="路由地址" prop="path" v-if="form.menuType !== 'F'">
          <el-input v-model="form.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="form.menuType === 'C'">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="权限标识" prop="perms" v-if="form.menuType !== 'M'">
          <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="50" />
        </el-form-item>
        <el-form-item label="显示状态" v-if="form.menuType !== 'F'">
          <el-radio-group v-model="form.visible">
            <el-radio label="0">显示</el-radio>
            <el-radio label="1">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
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
import { listMenu, getMenu, addMenu, updateMenu, deleteMenu } from '@/api/system/menu'
import type { MenuForm, MenuInfo } from '@/api/system/menu'

// 遮罩层
const loading = ref(false)
// 显示搜索条件
const showSearch = ref(true)
// 菜单表格数据
const menuList = ref<MenuInfo[]>([])
// 菜单树选项
const menuOptions = ref<MenuInfo[]>([])
// 弹出层标题
const title = ref('')
// 是否显示弹出层
const open = ref(false)

// 查询参数
const queryParams = reactive({
  menuName: undefined,
  status: undefined
})

// 表单参数
const form = reactive<MenuForm>({
  menuId: undefined,
  parentId: 0,
  menuName: '',
  orderNum: 0,
  path: '',
  component: '',
  query: '',
  isFrame: '1',
  isCache: '0',
  menuType: 'M',
  visible: '0',
  status: '0',
  perms: '',
  icon: '',
  remark: ''
})

// 表单校验
const rules = {
  menuName: [
    { required: true, message: '菜单名称不能为空', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '菜单顺序不能为空', trigger: 'blur' }
  ],
  path: [
    { required: true, message: '路由地址不能为空', trigger: 'blur' }
  ]
}

// 表单引用
const menuFormRef = ref()
const queryRef = ref()

/** 查询菜单列表 */
async function getList() {
  loading.value = true
  try {
    const res = await listMenu(queryParams)
    menuList.value = res.data
  } catch (error) {
    console.error('获取菜单列表失败:', error)
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
  form.menuId = undefined
  form.parentId = 0
  form.menuName = ''
  form.orderNum = 0
  form.path = ''
  form.component = ''
  form.query = ''
  form.isFrame = '1'
  form.isCache = '0'
  form.menuType = 'M'
  form.visible = '0'
  form.status = '0'
  form.perms = ''
  form.icon = ''
  form.remark = ''
  menuFormRef.value?.resetFields()
}

/** 搜索按钮操作 */
function handleQuery() {
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryRef.value?.resetFields()
  handleQuery()
}

/** 新增按钮操作 */
async function handleAdd(row?: MenuInfo) {
  reset()
  await getMenuTree()
  if (row) {
    form.parentId = row.menuId
  }
  open.value = true
  title.value = '添加菜单'
}

/** 修改按钮操作 */
async function handleUpdate(row: MenuInfo) {
  reset()
  await getMenuTree()
  const res = await getMenu(row.menuId)
  Object.assign(form, res.data)
  open.value = true
  title.value = '修改菜单'
}

/** 提交按钮 */
function submitForm() {
  menuFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (form.menuId) {
          await updateMenu(form)
          ElMessage.success('修改成功')
        } else {
          await addMenu(form)
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
function handleDelete(row: MenuInfo) {
  ElMessageBox.confirm('是否确认删除名称为"' + row.menuName + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.menuId)
      getList()
      ElMessage.success('删除成功')
    } catch (error: any) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

/** 获取菜单下拉树列表 */
async function getMenuTree() {
  try {
    const res = await listMenu()
    menuOptions.value = res.data
  } catch (error) {
    console.error('获取菜单树失败:', error)
  }
}

onMounted(() => {
  getList()
})
</script> 