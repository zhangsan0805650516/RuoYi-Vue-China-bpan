<template>
  <div class="app-container">

    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane :label="$t('全部')" name="all"></el-tab-pane>
      <el-tab-pane :label="$t('申购中')" name="doing"></el-tab-pane>
      <el-tab-pane :label="$t('中签')" name="success"></el-tab-pane>
      <el-tab-pane :label="$t('未中签')" name="fail"></el-tab-pane>
      <el-tab-pane :label="$t('弃购')" name="giveUp"></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('下级代理')" prop="dailiId">
        <el-cascader
          v-model="queryParams.dailiId"
          :options="dailiList"
          :props="{ value: 'userId', label: 'nickName',children: 'children', emitPath: false, checkStrictly: true }"
        ></el-cascader>
      </el-form-item>
      <el-form-item :label="$t('用户姓名')" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          :placeholder="$t('请输入用户姓名')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('手机号')" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          :placeholder="$t('请输入手机号')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('唯一码')" prop="weiyima">
        <el-input
          v-model="queryParams.weiyima"
          :placeholder="$t('请输入唯一码')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('股票代码')" prop="code">
        <el-input
          v-model="queryParams.code"
          :placeholder="$t('请输入股票代码')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('股票名称')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="$t('请输入股票名称')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('状态')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('申购中')" :value="0"/>
          <el-option :label="$t('中签')" :value="1"/>
          <el-option :label="$t('未中签')" :value="2"/>
          <el-option :label="$t('弃购')" :value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('认缴')" prop="renjiao">
        <el-select
          v-model="queryParams.renjiao"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('未认缴')" :value="0"/>
          <el-option :label="$t('已认缴')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('重置') }}</el-button>
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
          v-hasPermi="['biz:sgList:add']"
        >{{ $t('新增') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="allocation"
          v-hasPermi="['biz:sgList:edit']"
        >{{ $t('修改') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['biz:sgList:remove']"
        >{{ $t('删除') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:sgList:export']"
        >{{ $t('导出') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          size="mini"
          @click="transToHold"
          v-hasPermi="['biz:sgList:transToHold']"
        >{{ $t('一键转持仓') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sgListList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('操作')" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status !== 2 && scope.row.status !== 3 && scope.row.isCc === 0"
            size="mini"
            type="primary"
            @click="allocation(scope.row)"
            v-hasPermi="['biz:sgList:submitAllocation']"
          ><span style="margin: 5px">发布中签</span></el-button>
          <el-button
            v-if="scope.row.status === 1 && scope.row.renjiao === 0"
            size="mini"
            type="success"
            @click="subscription(scope.row)"
            v-hasPermi="['biz:sgList:subscriptionBg']"
          ><span style="margin: 5px">认缴</span></el-button>
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['biz:sgList:edit']"-->
<!--          >修改</el-button>-->
          <el-button
            v-if="scope.row.renjiao === 1 && scope.row.isCc === 0"
            size="mini"
            type="danger"
            @click="transOneToHold(scope.row)"
            v-hasPermi="['biz:sgList:transOneToHold']"
          ><span style="margin: 5px">转持仓</span></el-button>
<!--          <el-button-->
<!--            v-if="scope.row.status === 0 || scope.row.renjiao === 0"-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['biz:sgList:remove']"-->
<!--          >删除</el-button>-->
        </template>
      </el-table-column>
      <el-table-column :label="$t('上级姓名')" align="center" prop="faMember.superiorName" />
      <el-table-column :label="$t('用户姓名')" align="center" prop="faMember.name" />
      <el-table-column :label="$t('手机号')" align="center" prop="faMember.mobile" width="110" >
        <template slot-scope="scope">
          {{ scope.row.faMember.mobile ? scope.row.faMember.mobile.replace(/(\d{3})(\d{4})(\d{4})/, "$1****$3") : "" }}
        </template>
      </el-table-column>
<!--      <el-table-column label="股票代码" align="center" prop="code" />-->
      <el-table-column :label="$t('股票名称')" align="center" prop="name" />
      <el-table-column :label="$t('申购价')" align="center" prop="sgFxPrice" />
      <el-table-column :label=gqpeizhiSgName align="center" prop="sgNum" width="100"/>
      <el-table-column label="申购数(股)" align="center" prop="sgNums" width="100" />
      <el-table-column :label="$t('状态')" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0">{{ $t('申购中') }}</el-tag>
          <el-tag type="success" v-if="scope.row.status === 1">{{ $t('中签') }}</el-tag>
          <el-tag type="info" v-if="scope.row.status === 2">{{ $t('未中签') }}</el-tag>
          <el-tag type="warning" v-if="scope.row.status === 3">{{ $t('弃购') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('认缴')" align="center" prop="renjiao" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.renjiao === 0">{{ $t('未认缴') }}</el-tag>
          <el-tag type="success" v-if="scope.row.renjiao === 1">{{ $t('已认缴') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('认缴金额')" align="center" prop="zqMoney" />
<!--      <el-table-column label="保证金" align="center" prop="money" />-->
      <el-table-column :label=gqpeizhiZqName align="center" prop="zqNum" width="100"/>
      <el-table-column :label="$t('中签数(股)')" align="center" prop="zqNums" width="100" />
      <el-table-column :label="$t('转持仓')" align="center" prop="isCc" >
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.isCc === 0">{{ $t('未转') }}</el-tag>
          <el-tag type="success" v-if="scope.row.isCc === 1">{{ $t('已转') }}</el-tag>
          <el-tag type="info" v-if="scope.row.isCc === 2">{{ $t('弃转') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('创建时间')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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

    <!-- 添加或修改申购列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-form-item :label="$t('股票代码')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('请输入股票代码')" />
        </el-form-item>
        <el-form-item :label="$t('股票名称')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('请输入股票名称')" />
        </el-form-item>
        <el-form-item :label="$t('申购价')" prop="sgFxPrice">
          <el-input v-model="form.sgFxPrice" :placeholder="$t('请输入申购价')" />
        </el-form-item>
        <el-form-item :label="$t('市盈率')" prop="sgHyRate">
          <el-input v-model="form.sgHyRate" :placeholder="$t('请输入市盈率')" />
        </el-form-item>
        <el-form-item :label="$t('申购数量')" prop="sgNum">
          <el-input v-model="form.sgNum" :placeholder="$t('请输入申购数量')" />
        </el-form-item>
<!--        <el-form-item label="申购数量(股)" prop="sgNums">-->
<!--          <el-input v-model="form.sgNums" placeholder="请输入申购数量(股)" />-->
<!--        </el-form-item>-->
        <el-form-item :label="$t('最大手数')" prop="maxSg">
          <el-input v-model="form.maxSg" :placeholder="$t('请输入最大手数')" />
        </el-form-item>
        <el-form-item :label="$t('最大数量')" prop="maxSg">
          <el-input v-model="form.maxSg" :placeholder="$t('请输入最大数量')" />
        </el-form-item>
        <el-form-item :label="$t('状态')" prop="status">
          <el-select v-model="form.status" :placeholder="$t('请选择')" >
            <el-option :label="$t('申购中')" :value="0"/>
            <el-option :label="$t('中签')" :value="1"/>
            <el-option :label="$t('未中签')" :value="2"/>
            <el-option :label="$t('弃购')" :value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('保证金')" prop="status">
          <el-input v-model="form.status" :placeholder="$t('请输入申购数量')" />
        </el-form-item>
        <el-form-item :label="$t('中签数')" prop="zqNum">
          <el-input v-model="form.zqNum" :placeholder="$t('请输入中签数')" />
        </el-form-item>
        <el-form-item :label="$t('冻结资金')" prop="djMoney">
          <el-input v-model="form.djMoney" :placeholder="$t('请输入冻结资金')" />
        </el-form-item>
        <el-form-item :label="$t('申购日期')" prop="sgSgDate">
          <el-date-picker clearable
            v-model="form.sgSgDate"
            type="date"
            value-format="yyyy-MM-dd"
            :placeholder="$t('请选择')">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('中签缴款日期')" prop="sgZqJkDate">
          <el-date-picker clearable
            v-model="form.sgZqJkDate"
            type="date"
            value-format="yyyy-MM-dd"
            :placeholder="$t('请选择')">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('上市日期')" prop="sgSsDate">
          <el-date-picker clearable
            v-model="form.sgSsDate"
            type="date"
            value-format="yyyy-MM-dd"
            :placeholder="$t('请选择')">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改申购列表对话框 -->
    <el-dialog :title="title" :visible.sync="openAllocation" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rulesAllocation" label-width="200px">
        <el-form-item :label="$t('股票名称')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('请输入股票名称')" disabled />
        </el-form-item>
        <el-form-item :label="$t('申购价')" prop="sgFxPrice">
          <el-input v-model="form.sgFxPrice" :placeholder="$t('请输入申购价')" disabled />
        </el-form-item>
        <el-form-item :label="$t('申购数量(签)')" prop="sgNum" v-if="gqpeizhi == 1">
          <el-input v-model="form.sgNum" :placeholder="$t('请输入申购数量')" disabled />
        </el-form-item>
        <el-form-item :label="$t('申购数量(股)')" prop="sgNums" v-if="gqpeizhi == 0">
          <el-input v-model="form.sgNums" :placeholder="$t('请输入申购数量')" disabled />
        </el-form-item>
        <el-form-item :label="$t('状态')" prop="status">
          <el-select v-model="form.status" :placeholder="$t('请选择')">
            <el-option :label="$t('申购中')" :value="0"/>
            <el-option :label="$t('中签')" :value="1"/>
            <el-option :label="$t('未中签')" :value="2"/>
            <el-option :label="$t('弃购')" :value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('中签数(签)')" prop="zqNum" v-if="gqpeizhi == 1">
          <el-input v-model="form.zqNum" :placeholder="$t('请输入中签数')" />
          <div class="input-text" style="color: red; font-size: 12px">主板一签1000股，其他一签500股</div>
        </el-form-item>
        <el-form-item :label="$t('中签数(手)')" prop="zqNum" v-if="gqpeizhi == 0">
          <el-input v-model="form.zqNum" :placeholder="$t('请输入中签数')" />
          <div class="input-text" style="color: red; font-size: 12px">一手100股</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAllocation">{{ $t('确 定') }}</el-button>
        <el-button @click="cancelAllocation">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSgList, getSgList, delSgList, addSgList, updateSgList, submitAllocation, subscriptionBg, transToHold, transOneToHold } from "@/api/biz/sgList/sgList";
import { getDailiList } from "@/api/biz/member/member";
import { getRiskConfig } from "@/api/biz/riskConfig/riskConfig";

export default {
  name: "SgList",
  data() {
    return {
      gqpeizhiSgName: null,
      gqpeizhiZqName: null,
      gqpeizhi: null,
      activeName: "all",
      // 代理列表
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
      // 申购列表表格数据
      sgListList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openAllocation: false,
      // 查询参数
      queryParams: {
        dailiId: null,
        memberName: null,
        mobile: null,
        pageNum: 1,
        pageSize: 10,
        id: null,
        userId: null,
        shengouId: null,
        code: null,
        name: null,
        sgFxPrice: null,
        sgHyRate: null,
        sgNum: null,
        sgNums: null,
        maxSg: null,
        maxSgs: null,
        status: null,
        money: null,
        createTime: null,
        updateTime: null,
        zqNum: null,
        djMoney: null,
        sgSgDate: null,
        sgZqJkDate: null,
        sgSsDate: null,
        renjiao: null,
        renjiaoTime: null,
        isCc: null,
        isCcTime: null,
        zqMoney: null,
        zqNums: null,
        freezeProfit: null,
        balance: null,
        oldSgNum: null,
        oldSgNums: null,
        sgType: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      rulesAllocation: {
        zqNums: [
          { required: true, message: this.$t("请输入中签数"), trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDailiList();
    this.getRiskConfig(1005);
  },
  methods: {
    getRiskConfig(id) {
      getRiskConfig(id).then(res => {
        this.gqpeizhi = res.data.value;
        if (this.gqpeizhi == '0') {
          this.gqpeizhiSgName = '申购数(手)';
          this.gqpeizhiZqName = '中签数(手)';
        } else if (this.gqpeizhi == '1') {
          this.gqpeizhiSgName = '申购数(签)';
          this.gqpeizhiZqName = '中签数(签)';
        }
      }).catch(err => {
        console.log(err);
      })
    },
    transOneToHold(row) {
      this.$modal.confirm(this.$t('确定转持仓') + '？').then(function() {
        return transOneToHold({"id": row.id});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {});
    },
    transToHold() {
      this.$modal.confirm(this.$t('确定一键转持仓') + '？').then(function() {
        return transToHold();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {});
    },
    handleClick(tab, event) {
      if ("all" == tab.name) {
        this.resetForm("queryForm");
      } else if ("doing" == tab.name) {
        this.queryParams.status = 0;
      } else if ("success" == tab.name) {
        this.queryParams.status = 1;
      } else if ("fail" == tab.name) {
        this.queryParams.status = 2;
      } else if ("giveUp" == tab.name) {
        this.queryParams.status = 3;
      }
      this.handleQuery();
    },
    // 获取代理列表
    getDailiList() {
      getDailiList().then(response => {
        this.dailiList = response.data;
      });
    },
    /** 查询申购列表列表 */
    getList() {
      this.loading = true;
      listSgList(this.queryParams).then(response => {
        this.sgListList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelAllocation() {
      this.openAllocation = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        shengouId: null,
        code: null,
        name: null,
        sgFxPrice: null,
        sgHyRate: null,
        sgNum: null,
        sgNums: null,
        maxSg: null,
        status: null,
        money: null,
        createTime: null,
        updateTime: null,
        zqNum: null,
        djMoney: null,
        sgSgDate: null,
        sgZqJkDate: null,
        sgSsDate: null,
        renjiao: null,
        renjiaoTime: null,
        isCc: null,
        isCcTime: null,
        zqMoney: null,
        zqNums: null,
        freezeProfit: null,
        balance: null,
        oldSgNum: null,
        oldSgNums: null,
        sgType: null,
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
      this.title = this.$t("添加申购");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSgList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t("修改申购");
      });
    },
    /** 中签按钮操作 */
    allocation(row) {
      this.reset();
      const id = row.id || this.ids
      getSgList(id).then(response => {
        this.form = response.data;
        // this.form.maxSg = parseInt(response.data.faMember.balance / this.form.sgFxPrice / 100);
        // this.form.maxSgs = this.form.maxSg * 100;
        this.openAllocation = true;
        this.form.status = 1;
        this.title = this.$t("发布中签");
      });
    },
    /** 认缴按钮操作 */
    subscription(row) {
      this.$modal.confirm('确认要认缴吗？').then(() => {
        subscriptionBg({ "id" : row.id }).then(res => {
          this.$modal.msgSuccess("认缴成功");
          this.getList();
        }).catch(err => {
          this.$modal.msgError(err.message);
        });
      });
    },
    /** 提交中签 */
    submitAllocation() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 按股 sgNum
          if (this.gqpeizhi == 0) {
            if (this.form.status === 1 && this.form.zqNum <= 0) {
              this.$modal.msgError(this.$t("中签手数必须大于0"));
              return;
            }
            if (this.form.status === 1 && this.form.sgNum > 0) {
              if (this.form.zqNum > this.form.sgNum) {
                this.$modal.msgError(this.$t("中签手数超出申购手数"));
                return;
              }
            }
          }
          // 按签 sgNum
          else if (this.gqpeizhi == 1) {
            if (this.form.status === 1 && this.form.zqNum <= 0) {
              this.$modal.msgError(this.$t("中签签数必须大于0"));
              return;
            }
            if (this.form.status === 1 && this.form.sgNum > 0) {
              if (this.form.zqNum > this.form.sgNum) {
                this.$modal.msgError(this.$t("中签签数超出申购签数"));
                return;
              }
            }
          }

          submitAllocation(this.form).then(response => {
            this.$modal.msgSuccess(this.$t("成功"));
            this.openAllocation = false;
            this.getList();
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSgList(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.open = false;
              this.getList();
            });
          } else {
            addSgList(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
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
      this.$modal.confirm(this.$t("确认删除") + '？').then(function() {
        return delSgList(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/sgList/export', {
        ...this.queryParams
      }, `sgList_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
