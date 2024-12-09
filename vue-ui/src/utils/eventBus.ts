import mitt from 'mitt'

type Events = {
  'process:refresh': void
  'task:refresh': void
  'process:start': { processDefinitionId: string; businessKey: string }
  'task:complete': { taskId: string }
  'process:cancel': { processInstanceId: string }
}

export const workflowBus = mitt<Events>() 