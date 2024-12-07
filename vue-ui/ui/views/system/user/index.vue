<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listUser, getUser, addUser, updateUser, delUser, resetUserPwd, changeUserStatus } from '@/ui/api/system/user'
import { getConfigKey } from '@/ui/api/system/config'

// ... 其他代码保持不变
</script> 