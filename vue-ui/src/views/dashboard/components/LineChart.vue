<template>
  <div ref="chartRef" class="chart"></div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

const props = defineProps<{
  data: Array<{ date: string; todo: number; done: number }>
}>()

const chartRef = ref<HTMLElement>()
let chart: echarts.ECharts | null = null

function initChart() {
  if (!chartRef.value) return
  
  chart = echarts.init(chartRef.value)
  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['待办任务', '已办任务']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: props.data.map(item => item.date)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '待办任务',
        type: 'line',
        stack: 'Total',
        data: props.data.map(item => item.todo),
        smooth: true,
        areaStyle: {}
      },
      {
        name: '已办任务',
        type: 'line',
        stack: 'Total',
        data: props.data.map(item => item.done),
        smooth: true,
        areaStyle: {}
      }
    ]
  }
  
  chart.setOption(option)
}

function resizeChart() {
  chart?.resize()
}

watch(
  () => props.data,
  () => {
    initChart()
  }
)

onMounted(() => {
  initChart()
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  if (chart) {
    chart.dispose()
    chart = null
  }
  window.removeEventListener('resize', resizeChart)
})
</script>

<style lang="scss" scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style> 