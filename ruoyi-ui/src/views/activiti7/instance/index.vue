<template>
  <div class="app-container">
    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleUpload"
          v-hasPermi="['system:menu:add']"
        >上传流程文件</el-button>

        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="openBpmn"
          v-hasPermi="['system:menu:add']"
        >在线绘制流程</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table
      v-loading="loading"
      :data="instanceList"
    >
      <el-table-column prop="id" label="ID" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="name" label="实例名称" align="center" width="100">
      </el-table-column>
      <el-table-column prop="startDate" label="实例创建时间" width="60"></el-table-column>
      <el-table-column prop="processDefinitionId" label="流程定义编号" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="processDefinitionKey" label="流程定义KEY" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="processDefinitionVersion" label="版本" width="80"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini"
            type="text"
            icon="el-icon-edit"
            @click="doHangUp(scope.row)"
            v-hasPermi="['system:menu:edit']"
          >挂起</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="doActive(scope.row)"
            v-hasPermi="['system:menu:add']"
          >激活</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="historicView(scope.row)"
            v-hasPermi="['system:menu:add']"
          >历史</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="removeInc(scope.row)"
            v-hasPermi="['system:menu:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listInstance, doHangUp, doActive, removeInc } from "@/api/activiti7/instance";

export default {
  name: "Instance",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,

      instanceList: [],

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
    /** 查询部署列表 */
    getList() {
      this.loading = true;
      listInstance(this.queryParams).then(response => {
        this.instanceList = response.data;
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


// 挂起  instanceId: record.id
doHangUp(row) {
  doHangUp(row.id).then(response => {
            this.msgSuccess("流程实例挂起成功");

            this.getList();
          });
},

// 激活  instanceId: record.id
doActive(row) {
  doActive(row.id).then(response => {
            this.msgSuccess("流程实例激活成功");
            this.getList();
          });
},

// 删除  instanceId: record.id
removeInc(row) {
  removeInc(row.id).then(response => {
            this.msgSuccess("流程实例删除成功");

            this.getList();
          });
},

    // 历史
    historicView(data) {
        window.open('http://localhost:8080/bpmnjs/dist/index.html?type=lookBpmn&instanceId='+data.id+'&deploymentFileUUID='+data.deploymentId+'&deploymentName='+ encodeURI(data.resourceName)+'');
    }
  },

};
</script>
