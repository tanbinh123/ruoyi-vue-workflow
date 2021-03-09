<template>
  <div class="app-container">
    <el-table
      v-loading="loading"
      :data="taskList"
    >
      <el-table-column prop="id" label="ID" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="instanceName" label="流程名称" align="center" width="100">
      </el-table-column>
      <el-table-column prop="name" label="任务节点名称" width="60"></el-table-column>
      <el-table-column prop="status" label="任务状态" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="assignee" label="任务执行人" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="createdDate" label="创建时间" width="80"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleTask(scope.row)"
            v-hasPermi="['system:menu:edit']"
          >办理</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listTask, handleTask, doActive, removeInc } from "@/api/activiti7/task";

export default {
  name: "Task",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,

      taskList: [],

      // 查询参数
      queryParams: {
        current: 0,
        pageSize: 10
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
      listTask(this.queryParams).then(response => {
        this.taskList = response.data;
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


    // 办理任务
    handleTask(record) {
        handleTask(record.id).then(response => {
          this.msgSuccess("办理成功！");
          this.getList();
        });
    },
  },

};
</script>
