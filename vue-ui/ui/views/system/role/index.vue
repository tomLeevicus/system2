<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listRole, getRole, addRole, updateRole, delRole, changeRoleStatus, dataScope } from '@/ui/api/system/role'
import { treeselect as menuTreeselect, roleMenuTreeselect } from '@/ui/api/system/menu'
import { treeselect as deptTreeselect, roleDeptTreeselect } from '@/ui/api/system/dept'
// ... 其他代码保持不变
</script> 