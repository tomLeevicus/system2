export default {
  zh: {
    workflow: {
      process: {
        status: {
          draft: '草稿',
          running: '运行中',
          suspended: '已挂起',
          finished: '已完成',
          canceled: '已取消',
          terminated: '已终止',
          error: '错误'
        },
        category: {
          oa: 'OA办公',
          finance: '财务',
          hr: '人事'
        }
      },
      form: {
        status: {
          draft: '草稿',
          submitted: '已提交',
          approved: '已通过',
          rejected: '已拒绝'
        },
        type: {
          leave: '请假申请',
          expense: '报销申请',
          purchase: '采购申请'
        }
      },
      task: {
        type: {
          todo: '待办任务',
          done: '已办任务'
        },
        action: {
          approve: '同意',
          reject: '拒绝',
          delegate: '委派',
          transfer: '转办',
          comment: '审批意见'
        }
      },
      button: {
        start: '发起流程',
        save: '保存草稿',
        submit: '提交',
        approve: '同意',
        reject: '拒绝',
        cancel: '撤销',
        delegate: '委派',
        transfer: '转办'
      },
      message: {
        deploySuccess: '流程部署成功',
        startSuccess: '流程发起成功',
        saveSuccess: '保存成功',
        submitSuccess: '提交成功',
        approveSuccess: '审批完成',
        cancelSuccess: '流程已撤销',
        delegateSuccess: '任务已委派',
        transferSuccess: '任务已转办'
      }
    }
  }
} 