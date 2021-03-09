<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
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
    </el-row>

    <el-table
      v-loading="loading"
      :data="deploymentList"
    >
      <el-table-column prop="id" label="流程定义ID" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="deploymentId" label="部署ID" align="center" width="100">
      </el-table-column>
      <el-table-column prop="name" label="流程定义名称" width="60"></el-table-column>
      <el-table-column prop="resourceName" label="流程文件名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="key" label="KEY" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="version" label="部署版本" width="80"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleStartInstance(scope.row)"
            v-hasPermi="['system:menu:edit']"
          >启动实例</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleView(scope.row)"
            v-hasPermi="['system:menu:add']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:menu:remove']"
          >删除部署</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 上传流程文件对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="部署名称">
              <el-input v-model="form.processName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-upload
              class="upload-demo"
              action="#"
              :http-request="requestUpload">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件或bpmn文件</div>
            </el-upload>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpload">上 传</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 启动流程实例 -->
    <el-dialog title="启动流程实例" :visible.sync="startOpen" width="600px" append-to-body>
      <el-form ref="startForm" :model="startForm" :rules="startRules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="实例名称">
              <el-input v-model="startForm.instanceName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实例描述">
              <el-input v-model="startForm.desc"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelStart">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDeployment, startInstance, delDeployment, uploadFile } from "@/api/activiti7/deployment";

export default {
  name: "Deployment",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,

      deploymentList: [],
      fileList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      startOpen: false,
      // 查询参数
      queryParams: {
        current: 0,
        pageSize: 10
      },
      // 表单参数
      form: {
        processName: null,
        processFile: null
      },
      startForm: {
        processDefKey: null
      },
      // 表单校验
      startRules: {
        instanceName: [
          { required: true, message: "请填写流程实例名称", trigger: "change" }
        ],
      },

      // 表单校验
      rules: {
        processName: [
          { required: true, message: "请填写流程部署名称", trigger: "change" }
        ],
      },
      processFile: null,
    };
  },
  created() {
    this.getList();
  },
  methods: {


    // 覆盖默认的上传行为
    requestUpload(params) {
      this.form.processFile = params.file;
    },

    // 上传预处理
    // beforeUpload(file) {
    //   console.log("file......", file);
    //   const reader = new FileReader();
    //   // reader.readAsDataURL(file);
    //   reader.readAsBinaryString(file);
    //   reader.onload = () => {
    //     this.form.processFile = reader.result;
    //   };
    // },

    submitUpload() {
        // this.$refs.upload.submit();
        let formData = new FormData();
        formData.append("processName", this.form.processName);
        formData.append("processFile", this.form.processFile);
        uploadFile(formData).then(res => {
          this.msgSuccess("上传成功！");
          this.open = false;
          this.getList();
        });
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleSuccess(response, file, fileList) {
      this.msgSuccess("上传成功");
      this.open = false;
      this.getList();
    },

    /** 查询部署列表 */
    getList() {
      this.loading = true;
      listDeployment(this.queryParams).then(response => {
        this.deploymentList = response.data;
        this.loading = false;
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 取消按钮
    cancelStart() {
      this.startOpen = false;
    },
    // 表单重置
    reset() {
      this.form = {
        processName: null,
      };
      this.resetForm("form");
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
    /** 新增按钮操作 */
    handleUpload(row) {
      this.reset();
      this.open = true;
      this.title = "上传部署文件";
    },
    /** 修改按钮操作 */
    handleStartInstance(row) {
      this.startForm.processDefKey = row.key;
      this.startOpen = true;
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["startForm"].validate(valid => {
        if (valid) {
          startInstance(this.startForm).then(response => {
            this.msgSuccess("启动流程实例成功");
            this.startOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.name + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDeployment(row.deploymentId);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    // 在线绘制流程实例
    openBpmn() {
        // this.setState({filteredInfo: null});
        window.open("http://localhost:8080/bpmnjs/dist/index.html");
    },
    handleView(row) {
      window.open('http://localhost:8080/bpmnjs/dist/index.html?type=lookBpmn&deploymentFileUUID=' + row.deploymentId + '&deploymentName=' + encodeURI(row.resourceName)+'');
    }
  },

};
</script>
