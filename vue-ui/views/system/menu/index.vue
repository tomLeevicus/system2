<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <div class="right-menu">
            <el-button type="primary" @click="handleAdd">新增菜单</el-button>
          </div>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
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
              type="primary"
              link
              @click="handleAdd(scope.row)"
              v-if="scope.row.menuType != 'F'"
            >新增</el-button>
            <el-button
              type="primary"
              link
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              type="primary"
              link
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" v-model="open" width="680px" append-to-body>
      <el-form ref="menuForm" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="menuOptions"
            :props="{ label: 'menuName', value: 'menuId', children: 'children' }"
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
        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="路由地址" prop="path" v-if="form.menuType !== 'F'">
          <el-input v-model="form.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="form.menuType === 'C'">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="权限标识" prop="perms">
          <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="50" />
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon" v-if="form.menuType !== 'F'">
          <el-popover
            placement="bottom-start"
            :width="540"
            trigger="click"
          >
            <template #reference>
              <el-input v-model="form.icon" placeholder="点击选择图标" readonly>
                <template #prefix>
                  <el-icon v-if="form.icon">
                    <component :is="form.icon" />
                  </el-icon>
                </template>
              </el-input>
            </template>
            <icon-select ref="iconSelectRef" @selected="selected" />
          </el-popover>
        </el-form-item>
        <el-form-item label="显示状态">
          <el-radio-group v-model="form.visible">
            <el-radio
              v-for="dict in visibleOptions"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
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
import { listMenu, getMenu, addMenu, updateMenu, delMenu } from '@/api/system/menu'
import IconSelect from '@/components/IconSelect/index.vue'

// 遮罩层
const loading = ref(false)
// 菜单列表
const menuList = ref([])
// 菜单树选项
const menuOptions = ref([])
// 弹出层标题
const title = ref('')
// 是否显示弹出层
const open = ref(false)
// 显示状态数据字典
const visibleOptions = [
  { value: '0', label: '显示' },
  { value: '1', label: '隐藏' }
]
// 菜单状态数据字典
const statusOptions = [
  { value: '0', label: '正常' },
  { value: '1', label: '停用' }
]

// 查询参数
const queryParams = reactive({
  menuName: undefined,
  status: undefined
})

// 表单参数
const form = reactive({
  menuId: undefined,
  parentId: 0,
  menuName: undefined,
  orderNum: 0,
  path: undefined,
  component: undefined,
  perms: undefined,
  icon: undefined,
  menuType: 'M',
  visible: '0',
  status: '0'
})

// 表单校验
const rules = {
  menuName: [
    { required: true, message: '菜单名称不能为空', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '显示顺序不能为空', trigger: 'blur' }
  ],
  path: [
    { required: true, message: '路由地址不能为空', trigger: 'blur' }
  ]
}

/** 查询菜单列表 */
function getList() {
  loading.value = true
  listMenu(queryParams).then(response => {
    menuList.value = response.data
    loading.value = false
  })
}

/** 查询菜单下拉树结构 */
async function getTreeselect() {
  const response = await listMenu()
  menuOptions.value = [{
    menuId: 0,
    menuName: '主目录',
    children: response.data
  }]
}

/** 搜索按钮操作 */
function handleQuery() {
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.menuName = undefined
  queryParams.status = undefined
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd(row?: any) {
  reset()
  getTreeselect()
  if (row != null && row.menuId) {
    form.parentId = row.menuId
  }
  open.value = true
  title.value = '添加菜单'
}

/** 修改按钮操作 */
function handleUpdate(row: any) {
  reset()
  getTreeselect()
  getMenu(row.menuId).then(response => {
    Object.assign(form, response.data)
    open.value = true
    title.value = '修改菜单'
  })
}

/** 提交按钮 */
function submitForm() {
  menuForm.value.validate((valid: boolean) => {
    if (valid) {
      if (form.menuId) {
        updateMenu(form).then(response => {
          ElMessage.success('修改成功')
          open.value = false
          getList()
        })
      } else {
        addMenu(form).then(response => {
          ElMessage.success('新增成功')
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: any) {
  ElMessageBox.confirm('是否确认删除名称为"' + row.menuName + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delMenu(row.menuId)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  })
}

/** 选择图标 */
function selected(name: string) {
  form.icon = name
}

/** 表单重置 */
function reset() {
  form.menuId = undefined
  form.parentId = 0
  form.menuName = undefined
  form.orderNum = 0
  form.path = undefined
  form.component = undefined
  form.perms = undefined
  form.icon = undefined
  form.menuType = 'M'
  form.visible = '0'
  form.status = '0'
  menuForm.value?.resetFields()
}

/** 取消按钮 */
function cancel() {
  open.value = false
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