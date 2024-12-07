<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/ui/stores/user'
import { listProcess, deleteProcess, updateState, getProcessImage } from '@/ui/api/workflow/process'
// ... 其他代码保持不变
</script> 