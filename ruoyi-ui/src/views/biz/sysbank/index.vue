<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px" label-position="right">
      <el-col :span="6">
        <el-form-item label="收款名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入收款名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="收款银行" prop="bankInfo">
          <el-input
            v-model="queryParams.bankInfo"
            placeholder="请输入收款银行"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="收款账号" prop="account">
          <el-input
            v-model="queryParams.account"
            placeholder="请输入收款账号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="通道名称" prop="tdName">
          <el-input
            v-model="queryParams.tdName"
            placeholder="请输入通道名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="开户支行" prop="khzhihang">
          <el-input
            v-model="queryParams.khzhihang"
            placeholder="请输入开户支行"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['biz:sysbank:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['biz:sysbank:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['biz:sysbank:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:sysbank:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sysbankList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:sysbank:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:sysbank:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
      <el-table-column label="收款名称" align="center" prop="name" />
      <el-table-column label="收款银行" align="center" prop="bankInfo" />
      <el-table-column label="收款账号" align="center" prop="account" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0">禁用</span>
          <span v-if="scope.row.status === 1">正常</span>
        </template>
      </el-table-column>
      <el-table-column label="通道名称" align="center" prop="tdName" />
      <el-table-column label="开户支行" align="center" prop="khzhihang" />
      <el-table-column label="代理" align="center" prop="adminId" >
        <template slot-scope="scope">
          {{ scope.row.sysUser.nickName}}
        </template>
      </el-table-column>
      <el-table-column label="查看密码" align="center" prop="ckPass" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改通道对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="收款名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入收款名称" />
        </el-form-item>
        <el-form-item label="收款银行" prop="bankInfo">
          <el-input v-model="form.bankInfo" placeholder="请输入收款银行" />
        </el-form-item>
        <el-form-item label="收款账号" prop="account">
          <el-input v-model="form.account" placeholder="请输入收款账号" />
        </el-form-item>
        <el-form-item label="通道名称" prop="tdName">
          <el-input v-model="form.tdName" placeholder="请输入通道名称" />
        </el-form-item>
        <el-form-item label="开户支行" prop="khzhihang">
          <el-input v-model="form.khzhihang" placeholder="请输入开户支行" />
        </el-form-item>
        <el-form-item label="代理" prop="adminId">
          <el-cascader
            v-model="form.adminId"
            :options="dailiList"
            :props="{ value: 'userId', label: 'nickName',children: 'children', emitPath: false, checkStrictly: true }"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="查看密码" prop="ckPass">
          <el-input v-model="form.ckPass" placeholder="请输入查看密码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSysbank, getSysbank, delSysbank, addSysbank, updateSysbank } from "@/api/biz/sysbank/sysbank";
import { getDailiList } from "@/api/biz/member/member";

export default {
  name: "Sysbank",
  data() {
    return {
      dailiList: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 通道表格数据
      sysbankList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        name: null,
        bankInfo: null,
        account: null,
        status: null,
        createTime: null,
        updateTime: null,
        tdName: null,
        khzhihang: null,
        adminId: null,
        ckPass: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // name: [
        //   { required: true, message: "请输入收款名称", trigger: "blur" }
        // ],
        // bankInfo: [
        //   { required: true, message: "请输入收款银行", trigger: "blur" }
        // ],
        // account: [
        //   { required: true, message: "请输入收款账号", trigger: "blur" }
        // ],
        // khzhihang: [
        //   { required: true, message: "请输入开户支行", trigger: "blur" }
        // ],
        // adminId: [
        //   { required: true, message: "请选择代理", trigger: "change" }
        // ],
      }
    };
  },
  created() {
    this.getList();
    this.getDailiList();
  },
  methods: {
    // 获取代理列表
    getDailiList()
    {
      getDailiList().then(response => {
        this.dailiList = response.data;
      });
    },
    /** 查询通道列表 */
    getList() {
      this.loading = true;
      listSysbank(this.queryParams).then(response => {
        this.sysbankList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        bankInfo: null,
        account: null,
        status: null,
        createTime: null,
        updateTime: null,
        tdName: null,
        khzhihang: null,
        adminId: null,
        ckPass: null,
        deleteFlag: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加通道";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSysbank(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改通道";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSysbank(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSysbank(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除通道编号为"' + ids + '"的数据项？').then(function() {
        return delSysbank(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/sysbank/export', {
        ...this.queryParams
      }, `sysbank_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
