<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>字典数据</span>
          <div class="right-menu">
            <el-button type="primary" @click="handleAdd">新增字典数据</el-button>
            <el-button type="success" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="字典名称" prop="dictType">
          <el-select v-model="queryParams.dictType" placeholder="请选择字典类型">
            <el-option
              v-for="item in typeOptions"
              :key="item.dictId"
              :label="item.dictName"
              :value="item.dictType"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input
            v-model="queryParams.dictLabel"
            placeholder="请输入字典标签"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="数据状态" clearable>
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
        :data="dataList"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="字典编码" align="center" prop="dictCode" />
        <el-table-column label="字典标签" align="center" prop="dictLabel" />
        <el-table-column label="字典键值" align="center" prop="dictValue" />
        <el-table-column label="字典排序" align="center" prop="dictSort" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">
              {{ scope.row.status === '0' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="180">
          <template #default="scope">
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

      <!-- 分页区域 -->
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改字典数据对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="dictDataForm" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典类型">
          <el-input v-model="form.dictType" :disabled="true" />
        </el-form-item>
        <el-form-item label="数据标签" prop="dictLabel">
          <el-input v-model="form.dictLabel" placeholder="请输入数据标签" />
        </el-form-item>
        <el-form-item label="数据键值" prop="dictValue">
          <el-input v-model="form.dictValue" placeholder="请输入数据键值" />
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="form.dictSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listData, getData, addData, updateData, delData, exportData } from '@/api/system/dict/data'
import { listType } from '@/api/system/dict/type'

const route = useRoute()

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 字典数据表格数据
const dataList = ref([])
// 字典类型选项
const typeOptions = ref([])
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
  dictType: route.params.dictType,
  dictLabel: undefined,
  status: undefined
})

// 表单参数
const form = reactive({
  dictCode: undefined,
  dictType: undefined,
  dictLabel: undefined,
  dictValue: undefined,
  dictSort: 0,
  status: '0',
  remark: undefined
})

// 表单校验
const rules = {
  dictLabel: [
    { required: true, message: '数据标签不能为空', trigger: 'blur' }
  ],
  dictValue: [
    { required: true, message: '数据键值不能为空', trigger: 'blur' }
  ],
  dictSort: [
    { required: true, message: '数据顺序不能为空', trigger: 'blur' }
  ]
}

/** 查询字典类型列表 */
function getDictTypeList() {
  listType().then(response => {
    typeOptions.value = response.data
  })
}

/** 查询字典数据列表 */
function getList() {
  loading.value = true
  listData(queryParams).then(response => {
    dataList.value = response.rows
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
  queryParams.dictLabel = undefined
  queryParams.status = undefined
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  form.dictType = queryParams.dictType
  open.value = true
  title.value = '添加字典数据'
}

/** 修改按钮操作 */
function handleUpdate(row: any) {
  reset()
  const dictCode = row.dictCode
  getData(dictCode).then(response => {
    Object.assign(form, response.data)
    open.value = true
    title.value = '修改字典数据'
  })
}

/** 提交按钮 */
function submitForm() {
  dictDataForm.value.validate((valid: boolean) => {
    if (valid) {
      if (form.dictCode) {
        updateData(form).then(response => {
          ElMessage.success('修改成功')
          open.value = false
          getList()
        })
      } else {
        addData(form).then(response => {
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
  const dictCodes = [row.dictCode]
  ElMessageBox.confirm('是否确认删除字典编码为"' + dictCodes + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delData(dictCodes)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  })
}

/** 导出按钮操作 */
function handleExport() {
  exportData(queryParams)
}

/** 表单重置 */
function reset() {
  form.dictCode = undefined
  form.dictType = undefined
  form.dictLabel = undefined
  form.dictValue = undefined
  form.dictSort = 0
  form.status = '0'
  form.remark = undefined
  dictDataForm.value?.resetFields()
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

onMounted(() => {
  getDictTypeList()
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