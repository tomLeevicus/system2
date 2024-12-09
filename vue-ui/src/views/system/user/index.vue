<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['system:user:add']"
          >新增</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryFormRef" :inline="true">
        <el-form-item label="用户名称" prop="userName">
          <el-input
            v-model="queryParams.userName"
            placeholder="请输入用户名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input
            v-model="queryParams.phonenumber"
            placeholder="请输入手机号码"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="用户状态" clearable>
            <el-option label="正常" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="userList"
        row-key="userId"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户编号" align="center" prop="userId" />
        <el-table-column label="用户名称" align="center" prop="userName" />
        <el-table-column label="用户昵称" align="center" prop="nickName" />
        <el-table-column label="部门" align="center" prop="deptName" />
        <el-table-column label="手机号码" align="center" prop="phonenumber" />
        <el-table-column label="状态" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
              v-hasPermi="['system:user:edit']"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" />
        <el-table-column label="操作" align="center" width="180">
          <template #default="scope">
            <el-button
              type="primary"
              link
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:user:edit']"
            >修改</el-button>
            <el-button
              type="primary"
              link
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:user:delete']"
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
    </el-card>

    <!-- 添加或修改用户对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="600px"
      append-to-body
    >
      <el-form
        ref="userFormRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="用户昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入用户昵称" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="用户性别">
          <el-select v-model="form.sex">
            <el-option label="男" value="0" />
            <el-option label="女" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listUser, getUser, addUser, updateUser, deleteUser } from '@/api/system/user'
import type { UserInfo } from '@/types/system'

// 遮罩层
const loading = ref(false)
// 选中数组
const ids = ref<number[]>([])
// 总条数
const total = ref(0)
// 用户表格数据
const userList = ref<UserInfo[]>([])
// 弹出层标题
const title = ref('')
// 是否显示弹出层
const open = ref(false)

// 查询参数
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  userName: '',
  phonenumber: '',
  status: ''
})

// 表单参数
const form = ref<UserInfo>({
  userId: undefined,
  userName: '',
  nickName: '',
  deptId: undefined,
  email: '',
  phonenumber: '',
  sex: '0',
  status: '0'
})

// 表单校验
const rules = {
  userName: [
    { required: true, message: '用户名称不能为空', trigger: 'blur' }
  ],
  nickName: [
    { required: true, message: '用户昵称不能为空', trigger: 'blur' }
  ]
}

/** 查询用户列表 */
async function getList() {
  loading.value = true
  try {
    const res = await listUser(queryParams.value)
    userList.value = res.data.rows
    total.value = res.data.total
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
  form.value = {
    userId: undefined,
    userName: '',
    nickName: '',
    deptId: undefined,
    email: '',
    phonenumber: '',
    sex: '0',
    status: '0'
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    userName: '',
    phonenumber: '',
    status: ''
  }
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = '添加用户'
}

/** 修改按钮操作 */
async function handleUpdate(row: UserInfo) {
  reset()
  const userId = row.userId
  const res = await getUser(userId)
  Object.assign(form.value, res.data)
  open.value = true
  title.value = '修改用户'
}

/** 提交按钮 */
function submitForm() {
  // TODO: 表单验证
  if (form.value.userId) {
    updateUser(form.value).then(() => {
      ElMessage.success('修改成功')
      open.value = false
      getList()
    })
  } else {
    addUser(form.value).then(() => {
      ElMessage.success('新增成功')
      open.value = false
      getList()
    })
  }
}

/** 删除按钮操作 */
function handleDelete(row: UserInfo) {
  const userIds = row.userId ? [row.userId] : ids.value
  
  ElMessageBox.confirm('是否确认删除所选用户?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteUser(userIds)
    getList()
    ElMessage.success('删除成功')
  })
}

/** 用户状态修改 */
async function handleStatusChange(row: UserInfo) {
  const text = row.status === '0' ? '启用' : '停用'
  try {
    await updateUser(row)
    ElMessage.success(text + '成功')
  } catch (error) {
    row.status = row.status === '0' ? '1' : '0'
  }
}

onMounted(() => {
  getList()
})
</script> 