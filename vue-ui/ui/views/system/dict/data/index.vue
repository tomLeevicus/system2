<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listData, getData, addData, updateData, delData, exportData } from '@/ui/api/system/dict/data'
import { listType } from '@/ui/api/system/dict/type'

// ... 其他代码保持不变
</script> 