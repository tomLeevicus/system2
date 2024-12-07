<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <div class="right-menu">
            <el-button type="primary" @click="handleAdd">新增用户</el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="用户名称" prop="username">
          <el-input
            v-model="queryParams.username"
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
          <el-select v-model="queryParams.status" placeholder="用���状态" clearable>
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
        :data="userList"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户编号" align="center" prop="userId" />
        <el-table-column label="用户名称" align="center" prop="username" />
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
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" />
        <el-table-column label="操作" align="center" width="180">
          <template #default="scope">
            <el-button
              type="text"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              type="text"
              @click="handleDelete(scope.row)"
              v-if="scope.row.userId !== 1"
            >删除</el-button>
            <el-button
              type="text"
              @click="handleResetPwd(scope.row)"
            >重置密码</el-button>
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

    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="userForm" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名称" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名称" />
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
import { listUser, getUser, addUser, updateUser, delUser, resetUserPwd, changeUserStatus } from '@/api/system/user'

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 用户表格数据
const userList = ref([])
// 弹出层标题
const title = ref('')
// 是否显示弹出层
const open = ref(false)
// 状态数据字典
const statusOptions = [
  { value: '0', label: '正常' },
  { value: '1', label: '停用' }
]

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: undefined,
  phonenumber: undefined,
  status: undefined
})

// 表单参数
const form = reactive({
  userId: undefined,
  username: undefined,
  nickName: undefined,
  password: undefined,
  phonenumber: undefined,
  email: undefined,
  sex: '0',
  status: '0'
})

// 表单校验
const rules = {
  username: [
    { required: true, message: '用户名称不能为空', trigger: 'blur' }
  ],
  nickName: [
    { required: true, message: '用户昵称不能为空', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '用户密码不能为空', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ],
  phonenumber: [
    { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

/** 查询用户列表 */
function getList() {
  loading.value = true
  listUser(queryParams).then(response => {
    userList.value = response.rows
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
  queryParams.username = undefined
  queryParams.phonenumber = undefined
  queryParams.status = undefined
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = '添加用户'
}

/** 修改按钮操作 */
function handleUpdate(row: any) {
  reset()
  const userId = row.userId || row.id
  getUser(userId).then(response => {
    Object.assign(form, response.data)
    open.value = true
    title.value = '修改用户'
  })
}

/** 提交按钮 */
function submitForm() {
  userForm.value.validate((valid: boolean) => {
    if (valid) {
      if (form.userId) {
        updateUser(form).then(response => {
          ElMessage.success('修改成功')
          open.value = false
          getList()
        })
      } else {
        addUser(form).then(response => {
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
  const userIds = row.userId || row.id
  ElMessageBox.confirm('是否确认删除用户编号为"' + userIds + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delUser(userIds)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  })
}

/** 重置密码按钮操作 */
function handleResetPwd(row: any) {
  ElMessageBox.prompt('请输入"' + row.username + '"的新密码', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(({ value }) => {
    resetUserPwd(row.userId, value).then(response => {
      ElMessage.success('修改成功')
    })
  })
}

/** 用户状态修改 */
function handleStatusChange(row: any) {
  const text = row.status === '0' ? '启用' : '停用'
  ElMessageBox.confirm('确认要"' + text + '""' + row.username + '"用户吗?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return changeUserStatus(row.userId, row.status)
  }).then(() => {
    ElMessage.success(text + '成功')
  }).catch(() => {
    row.status = row.status === '0' ? '1' : '0'
  })
}

/** 表单重置 */
function reset() {
  form.userId = undefined
  form.username = undefined
  form.nickName = undefined
  form.password = undefined
  form.phonenumber = undefined
  form.email = undefined
  form.sex = '0'
  form.status = '0'
  userForm.value?.resetFields()
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