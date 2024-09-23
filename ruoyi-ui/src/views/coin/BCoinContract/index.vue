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
      <el-form-item label="币种名称" prop="coinName">
        <el-input
          v-model="queryParams.coinName"
          placeholder="请输入币种名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="币种英文名称" prop="coinNameEn">
        <el-input
          v-model="queryParams.coinNameEn"
          placeholder="请输入币种英文名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="币种code" prop="coinCode">
        <el-input
          v-model="queryParams.coinCode"
          placeholder="请输入币种code"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="币种介绍" prop="coinDescribe">
        <el-input
          v-model="queryParams.coinDescribe"
          placeholder="请输入币种介绍"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="币种图标" prop="coinIcon">
        <el-input
          v-model="queryParams.coinIcon"
          placeholder="请输入币种图标"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="币种排序" prop="coinSort">
        <el-input
          v-model="queryParams.coinSort"
          placeholder="请输入币种排序"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发行总量" prop="coinFxTotalNum">
        <el-input
          v-model="queryParams.coinFxTotalNum"
          placeholder="请输入发行总量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流通总量" prop="coinLtTotalNum">
        <el-input
          v-model="queryParams.coinLtTotalNum"
          placeholder="请输入流通总量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发行价格" prop="coinFxPrice">
        <el-input
          v-model="queryParams.coinFxPrice"
          placeholder="请输入发行价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发行时间" prop="coinFxTime">
        <el-date-picker clearable
          v-model="queryParams.coinFxTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择发行时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="白皮书链接" prop="coinWhitePaperLink">
        <el-input
          v-model="queryParams.coinWhitePaperLink"
          placeholder="请输入白皮书链接"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="官网链接" prop="coinOfficialWebsiteLink">
        <el-input
          v-model="queryParams.coinOfficialWebsiteLink"
          placeholder="请输入官网链接"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提币手续费" prop="coinWithdrawFee">
        <el-input
          v-model="queryParams.coinWithdrawFee"
          placeholder="请输入提币手续费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提币最小数量" prop="coinWithdrawLimitDown">
        <el-input
          v-model="queryParams.coinWithdrawLimitDown"
          placeholder="请输入提币最小数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提币最大数量" prop="coinWithdrawLimitUp">
        <el-input
          v-model="queryParams.coinWithdrawLimitUp"
          placeholder="请输入提币最大数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示状态" prop="isShow">
        <el-input
          v-model="queryParams.isShow"
          placeholder="请输入显示状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否插针" prop="isPin">
        <el-input
          v-model="queryParams.isPin"
          placeholder="请输入是否插针"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建时间">
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
      <el-form-item label="采集-当前价格" prop="caiPrice">
        <el-input
          v-model="queryParams.caiPrice"
          placeholder="请输入采集-当前价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-涨跌" prop="caiPricechange">
        <el-input
          v-model="queryParams.caiPricechange"
          placeholder="请输入采集-涨跌"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-涨跌幅" prop="caiChangepercent">
        <el-input
          v-model="queryParams.caiChangepercent"
          placeholder="请输入采集-涨跌幅"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-买入价" prop="caiBuy">
        <el-input
          v-model="queryParams.caiBuy"
          placeholder="请输入采集-买入价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-卖出价" prop="caiSell">
        <el-input
          v-model="queryParams.caiSell"
          placeholder="请输入采集-卖出价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-昨收价" prop="caiSettlement">
        <el-input
          v-model="queryParams.caiSettlement"
          placeholder="请输入采集-昨收价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-今开价" prop="caiOpen">
        <el-input
          v-model="queryParams.caiOpen"
          placeholder="请输入采集-今开价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-最高价" prop="caiHigh">
        <el-input
          v-model="queryParams.caiHigh"
          placeholder="请输入采集-最高价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-最低价" prop="caiLow">
        <el-input
          v-model="queryParams.caiLow"
          placeholder="请输入采集-最低价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-成交量" prop="caiVolume">
        <el-input
          v-model="queryParams.caiVolume"
          placeholder="请输入采集-成交量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-成交量u" prop="caiVolumeUsdt">
        <el-input
          v-model="queryParams.caiVolumeUsdt"
          placeholder="请输入采集-成交量u"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-成交额" prop="caiAmount">
        <el-input
          v-model="queryParams.caiAmount"
          placeholder="请输入采集-成交额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集-成交额u" prop="caiAmountUsdt">
        <el-input
          v-model="queryParams.caiAmountUsdt"
          placeholder="请输入采集-成交额u"
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
          v-hasPermi="['coin:BCoinContract:add']"
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
          v-hasPermi="['coin:BCoinContract:edit']"
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
          v-hasPermi="['coin:BCoinContract:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['coin:BCoinContract:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="BCoinContractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="币种名称" align="center" prop="coinName" />
      <el-table-column label="币种英文名称" align="center" prop="coinNameEn" />
      <el-table-column label="币种code" align="center" prop="coinCode" />
      <el-table-column label="币种介绍" align="center" prop="coinDescribe" />
      <el-table-column label="币种图标" align="center" prop="coinIcon" />
      <el-table-column label="币种排序" align="center" prop="coinSort" />
      <el-table-column label="发行总量" align="center" prop="coinFxTotalNum" />
      <el-table-column label="流通总量" align="center" prop="coinLtTotalNum" />
      <el-table-column label="发行价格" align="center" prop="coinFxPrice" />
      <el-table-column label="发行时间" align="center" prop="coinFxTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.coinFxTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="白皮书链接" align="center" prop="coinWhitePaperLink" />
      <el-table-column label="官网链接" align="center" prop="coinOfficialWebsiteLink" />
      <el-table-column label="提币手续费" align="center" prop="coinWithdrawFee" />
      <el-table-column label="提币最小数量" align="center" prop="coinWithdrawLimitDown" />
      <el-table-column label="提币最大数量" align="center" prop="coinWithdrawLimitUp" />
      <el-table-column label="显示状态" align="center" prop="isShow" />
      <el-table-column label="是否插针" align="center" prop="isPin" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
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
      <el-table-column label="采集-当前价格" align="center" prop="caiPrice" />
      <el-table-column label="采集-涨跌" align="center" prop="caiPricechange" />
      <el-table-column label="采集-涨跌幅" align="center" prop="caiChangepercent" />
      <el-table-column label="采集-买入价" align="center" prop="caiBuy" />
      <el-table-column label="采集-卖出价" align="center" prop="caiSell" />
      <el-table-column label="采集-昨收价" align="center" prop="caiSettlement" />
      <el-table-column label="采集-今开价" align="center" prop="caiOpen" />
      <el-table-column label="采集-最高价" align="center" prop="caiHigh" />
      <el-table-column label="采集-最低价" align="center" prop="caiLow" />
      <el-table-column label="采集-成交量" align="center" prop="caiVolume" />
      <el-table-column label="采集-成交量u" align="center" prop="caiVolumeUsdt" />
      <el-table-column label="采集-成交额" align="center" prop="caiAmount" />
      <el-table-column label="采集-成交额u" align="center" prop="caiAmountUsdt" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['coin:BCoinContract:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['coin:BCoinContract:remove']"
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

    <!-- 添加或修改合约交易对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="币种名称" prop="coinName">
          <el-input v-model="form.coinName" placeholder="请输入币种名称" />
        </el-form-item>
        <el-form-item label="币种英文名称" prop="coinNameEn">
          <el-input v-model="form.coinNameEn" placeholder="请输入币种英文名称" />
        </el-form-item>
        <el-form-item label="币种code" prop="coinCode">
          <el-input v-model="form.coinCode" placeholder="请输入币种code" />
        </el-form-item>
        <el-form-item label="币种介绍" prop="coinDescribe">
          <el-input v-model="form.coinDescribe" placeholder="请输入币种介绍" />
        </el-form-item>
        <el-form-item label="币种图标" prop="coinIcon">
          <el-input v-model="form.coinIcon" placeholder="请输入币种图标" />
        </el-form-item>
        <el-form-item label="币种排序" prop="coinSort">
          <el-input v-model="form.coinSort" placeholder="请输入币种排序" />
        </el-form-item>
        <el-form-item label="发行总量" prop="coinFxTotalNum">
          <el-input v-model="form.coinFxTotalNum" placeholder="请输入发行总量" />
        </el-form-item>
        <el-form-item label="流通总量" prop="coinLtTotalNum">
          <el-input v-model="form.coinLtTotalNum" placeholder="请输入流通总量" />
        </el-form-item>
        <el-form-item label="发行价格" prop="coinFxPrice">
          <el-input v-model="form.coinFxPrice" placeholder="请输入发行价格" />
        </el-form-item>
        <el-form-item label="发行时间" prop="coinFxTime">
          <el-date-picker clearable
            v-model="form.coinFxTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择发行时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="白皮书链接" prop="coinWhitePaperLink">
          <el-input v-model="form.coinWhitePaperLink" placeholder="请输入白皮书链接" />
        </el-form-item>
        <el-form-item label="官网链接" prop="coinOfficialWebsiteLink">
          <el-input v-model="form.coinOfficialWebsiteLink" placeholder="请输入官网链接" />
        </el-form-item>
        <el-form-item label="提币手续费" prop="coinWithdrawFee">
          <el-input v-model="form.coinWithdrawFee" placeholder="请输入提币手续费" />
        </el-form-item>
        <el-form-item label="提币最小数量" prop="coinWithdrawLimitDown">
          <el-input v-model="form.coinWithdrawLimitDown" placeholder="请输入提币最小数量" />
        </el-form-item>
        <el-form-item label="提币最大数量" prop="coinWithdrawLimitUp">
          <el-input v-model="form.coinWithdrawLimitUp" placeholder="请输入提币最大数量" />
        </el-form-item>
        <el-form-item label="显示状态" prop="isShow">
          <el-input v-model="form.isShow" placeholder="请输入显示状态" />
        </el-form-item>
        <el-form-item label="是否插针" prop="isPin">
          <el-input v-model="form.isPin" placeholder="请输入是否插针" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="删除标记" prop="deleteFlag">
          <el-input v-model="form.deleteFlag" placeholder="请输入删除标记" />
        </el-form-item>
        <el-form-item label="采集-当前价格" prop="caiPrice">
          <el-input v-model="form.caiPrice" placeholder="请输入采集-当前价格" />
        </el-form-item>
        <el-form-item label="采集-涨跌" prop="caiPricechange">
          <el-input v-model="form.caiPricechange" placeholder="请输入采集-涨跌" />
        </el-form-item>
        <el-form-item label="采集-涨跌幅" prop="caiChangepercent">
          <el-input v-model="form.caiChangepercent" placeholder="请输入采集-涨跌幅" />
        </el-form-item>
        <el-form-item label="采集-买入价" prop="caiBuy">
          <el-input v-model="form.caiBuy" placeholder="请输入采集-买入价" />
        </el-form-item>
        <el-form-item label="采集-卖出价" prop="caiSell">
          <el-input v-model="form.caiSell" placeholder="请输入采集-卖出价" />
        </el-form-item>
        <el-form-item label="采集-昨收价" prop="caiSettlement">
          <el-input v-model="form.caiSettlement" placeholder="请输入采集-昨收价" />
        </el-form-item>
        <el-form-item label="采集-今开价" prop="caiOpen">
          <el-input v-model="form.caiOpen" placeholder="请输入采集-今开价" />
        </el-form-item>
        <el-form-item label="采集-最高价" prop="caiHigh">
          <el-input v-model="form.caiHigh" placeholder="请输入采集-最高价" />
        </el-form-item>
        <el-form-item label="采集-最低价" prop="caiLow">
          <el-input v-model="form.caiLow" placeholder="请输入采集-最低价" />
        </el-form-item>
        <el-form-item label="采集-成交量" prop="caiVolume">
          <el-input v-model="form.caiVolume" placeholder="请输入采集-成交量" />
        </el-form-item>
        <el-form-item label="采集-成交量u" prop="caiVolumeUsdt">
          <el-input v-model="form.caiVolumeUsdt" placeholder="请输入采集-成交量u" />
        </el-form-item>
        <el-form-item label="采集-成交额" prop="caiAmount">
          <el-input v-model="form.caiAmount" placeholder="请输入采集-成交额" />
        </el-form-item>
        <el-form-item label="采集-成交额u" prop="caiAmountUsdt">
          <el-input v-model="form.caiAmountUsdt" placeholder="请输入采集-成交额u" />
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
import { listBCoinContract, getBCoinContract, delBCoinContract, addBCoinContract, updateBCoinContract } from "@/api/coin/BCoinContract/BCoinContract";

export default {
  name: "BCoinContract",
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
      // 合约交易表格数据
      BCoinContractList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        coinName: null,
        coinNameEn: null,
        coinCode: null,
        coinDescribe: null,
        coinIcon: null,
        coinSort: null,
        coinFxTotalNum: null,
        coinLtTotalNum: null,
        coinFxPrice: null,
        coinFxTime: null,
        coinWhitePaperLink: null,
        coinOfficialWebsiteLink: null,
        coinWithdrawFee: null,
        coinWithdrawLimitDown: null,
        coinWithdrawLimitUp: null,
        isShow: null,
        isPin: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
        deleteFlag: null,
        caiPrice: null,
        caiPricechange: null,
        caiChangepercent: null,
        caiBuy: null,
        caiSell: null,
        caiSettlement: null,
        caiOpen: null,
        caiHigh: null,
        caiLow: null,
        caiVolume: null,
        caiVolumeUsdt: null,
        caiAmount: null,
        caiAmountUsdt: null
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
    /** 查询合约交易列表 */
    getList() {
      this.loading = true;
      listBCoinContract(this.queryParams).then(response => {
        this.BCoinContractList = response.rows;
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
        coinName: null,
        coinNameEn: null,
        coinCode: null,
        coinDescribe: null,
        coinIcon: null,
        coinSort: null,
        coinFxTotalNum: null,
        coinLtTotalNum: null,
        coinFxPrice: null,
        coinFxTime: null,
        coinWhitePaperLink: null,
        coinOfficialWebsiteLink: null,
        coinWithdrawFee: null,
        coinWithdrawLimitDown: null,
        coinWithdrawLimitUp: null,
        isShow: null,
        isPin: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
        deleteFlag: null,
        caiPrice: null,
        caiPricechange: null,
        caiChangepercent: null,
        caiBuy: null,
        caiSell: null,
        caiSettlement: null,
        caiOpen: null,
        caiHigh: null,
        caiLow: null,
        caiVolume: null,
        caiVolumeUsdt: null,
        caiAmount: null,
        caiAmountUsdt: null
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
      this.title = "添加合约交易";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBCoinContract(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改合约交易";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBCoinContract(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBCoinContract(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除合约交易编号为"' + ids + '"的数据项？').then(function() {
        return delBCoinContract(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('coin/BCoinContract/export', {
        ...this.queryParams
      }, `BCoinContract_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
