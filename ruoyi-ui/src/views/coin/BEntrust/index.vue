<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="id" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="委托流水号" prop="entrustNo">
        <el-input
          v-model="queryParams.entrustNo"
          placeholder="请输入委托流水号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="现货/合约id" prop="coinId">
        <el-input
          v-model="queryParams.coinId"
          placeholder="请输入现货/合约id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="委托价格" prop="entrustPrice">
        <el-input
          v-model="queryParams.entrustPrice"
          placeholder="请输入委托价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="委托数量" prop="entrustNumber">
        <el-input
          v-model="queryParams.entrustNumber"
          placeholder="请输入委托数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易数量" prop="tradeNumber">
        <el-input
          v-model="queryParams.tradeNumber"
          placeholder="请输入交易数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="撤销时间" prop="undoTime">
        <el-date-picker clearable
          v-model="queryParams.undoTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择撤销时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="委托状态(0委托中 1成交 2撤回 3部分成交)" prop="entrustState">
        <el-input
          v-model="queryParams.entrustState"
          placeholder="请输入委托状态(0委托中 1成交 2撤回 3部分成交)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="方向(1买涨 2买跌)" prop="tradeDirect">
        <el-input
          v-model="queryParams.tradeDirect"
          placeholder="请输入方向(1买涨 2买跌)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="添加时间" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择添加时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="创建者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入创建者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新时间" prop="updateTime">
        <el-date-picker clearable
          v-model="queryParams.updateTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择更新时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新者" prop="updateBy">
        <el-input
          v-model="queryParams.updateBy"
          placeholder="请输入更新者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="queryParams.remarks"
          placeholder="请输入备注"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="删除标记" prop="deleteFlag">
        <el-input
          v-model="queryParams.deleteFlag"
          placeholder="请输入删除标记"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
          v-hasPermi="['coin:BEntrust:add']"
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
          v-hasPermi="['coin:BEntrust:edit']"
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
          v-hasPermi="['coin:BEntrust:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['coin:BEntrust:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="BEntrustList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="委托流水号" align="center" prop="entrustNo" />
      <el-table-column label="用户id" align="center" prop="userId" />
      <el-table-column label="现货/合约id" align="center" prop="coinId" />
      <el-table-column label="交易类型(1币 2现货 3合约)" align="center" prop="coinType" />
      <el-table-column label="委托价格" align="center" prop="entrustPrice" />
      <el-table-column label="委托数量" align="center" prop="entrustNumber" />
      <el-table-column label="交易数量" align="center" prop="tradeNumber" />
      <el-table-column label="撤销时间" align="center" prop="undoTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.undoTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="委托状态(0委托中 1成交 2撤回 3部分成交)" align="center" prop="entrustState" />
      <el-table-column label="买卖(1买 2卖)" align="center" prop="tradingType" />
      <el-table-column label="方向(1买涨 2买跌)" align="center" prop="tradeDirect" />
      <el-table-column label="添加时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新者" align="center" prop="updateBy" />
      <el-table-column label="备注" align="center" prop="remarks" />
      <el-table-column label="删除标记" align="center" prop="deleteFlag" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['coin:BEntrust:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['coin:BEntrust:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改委托对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="委托流水号" prop="entrustNo">
          <el-input v-model="form.entrustNo" placeholder="请输入委托流水号" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="现货/合约id" prop="coinId">
          <el-input v-model="form.coinId" placeholder="请输入现货/合约id" />
        </el-form-item>
        <el-form-item label="委托价格" prop="entrustPrice">
          <el-input v-model="form.entrustPrice" placeholder="请输入委托价格" />
        </el-form-item>
        <el-form-item label="委托数量" prop="entrustNumber">
          <el-input v-model="form.entrustNumber" placeholder="请输入委托数量" />
        </el-form-item>
        <el-form-item label="交易数量" prop="tradeNumber">
          <el-input v-model="form.tradeNumber" placeholder="请输入交易数量" />
        </el-form-item>
        <el-form-item label="撤销时间" prop="undoTime">
          <el-date-picker clearable
            v-model="form.undoTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择撤销时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="委托状态(0委托中 1成交 2撤回 3部分成交)" prop="entrustState">
          <el-input v-model="form.entrustState" placeholder="请输入委托状态(0委托中 1成交 2撤回 3部分成交)" />
        </el-form-item>
        <el-form-item label="方向(1买涨 2买跌)" prop="tradeDirect">
          <el-input v-model="form.tradeDirect" placeholder="请输入方向(1买涨 2买跌)" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="删除标记" prop="deleteFlag">
          <el-input v-model="form.deleteFlag" placeholder="请输入删除标记" />
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
import { listBEntrust, getBEntrust, delBEntrust, addBEntrust, updateBEntrust } from "@/api/coin/BEntrust/BEntrust";

export default {
  name: "BEntrust",
  data() {
    return {
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
      // 委托表格数据
      BEntrustList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        entrustNo: null,
        userId: null,
        coinId: null,
        coinType: null,
        entrustPrice: null,
        entrustNumber: null,
        tradeNumber: null,
        undoTime: null,
        entrustState: null,
        tradingType: null,
        tradeDirect: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询委托列表 */
    getList() {
      this.loading = true;
      listBEntrust(this.queryParams).then(response => {
        this.BEntrustList = response.rows;
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
        entrustNo: null,
        userId: null,
        coinId: null,
        coinType: null,
        entrustPrice: null,
        entrustNumber: null,
        tradeNumber: null,
        undoTime: null,
        entrustState: null,
        tradingType: null,
        tradeDirect: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
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
      this.title = "添加委托";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBEntrust(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改委托";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBEntrust(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBEntrust(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除委托编号为"' + ids + '"的数据项？').then(function() {
        return delBEntrust(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('coin/BEntrust/export', {
        ...this.queryParams
      }, `BEntrust_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
