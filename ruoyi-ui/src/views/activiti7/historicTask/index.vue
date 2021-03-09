<template>
  <div class="app-container">
    <el-table
      v-loading="loading"
      :data="historicTaskList"
    >
      <el-table-column prop="id" label="ID" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="taskDefinitionKey" label="KEY" align="center" width="100">
      </el-table-column>
      <el-table-column prop="name" label="流程名称" width="60"></el-table-column>
      <el-table-column prop="processInstanceId" label="实例ID" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="80"></el-table-column>
      <el-table-column prop="assignee" label="办理人" width="80"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listHistoricTask } from "@/api/activiti7/historicTask";

export default {
  name: "HistoricTask",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,

      historicTaskList: [],

      // 查询参数
      queryParams: {
        current: 0,
        pageSize: 10,
        assignee: "",
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询任务列表 */
    getList() {
      this.loading = true;
      listHistoricTask(this.queryParams).then(response => {
        this.historicTaskList = response.data;
        this.loading = false;
      });
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
  },

};
</script>
