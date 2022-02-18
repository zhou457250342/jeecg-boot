<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <div class="title">主系统账单</div>
      <s-table style="margin-bottom: 24px" :columns="goodsColumns" :data="mainData"> </s-table>

      <div class="title">支付系统账单</div>
      <s-table style="margin-bottom: 24px" :columns="goodsColumns" :data="sideData">
        <template slot="status" slot-scope="status">
          <a-badge :status="status" :text="status | statusFilter" />
        </template>
      </s-table>
    </a-card>
  </page-layout>
</template>

<script>
import PageLayout from '@/components/page/PageLayout'
import STable from '@/components/table/'
import DetailList from '@/components/tools/DetailList'
import ABadge from 'ant-design-vue/es/badge/Badge'
import { httpAction, getAction } from '@/api/manage'
const DetailListItem = DetailList.Item

export default {
  components: {
    PageLayout,
    ABadge,
    DetailList,
    DetailListItem,
    STable
  },
  data() {
    return {
      goodsColumns: [
        {
          title: '名称',
          dataIndex: 'note',
          key: 'note'
        },
        {
          title: '金额',
          dataIndex: 'amount',
          key: 'amount'
        },
        {
          title: '交易时间',
          dataIndex: 'createTime',
          key: 'createTime'
        },
        {
          title: '业务流水号',
          dataIndex: 'operationNo',
          key: 'operationNo'
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      sideData: () => {
        return new Promise(resolve => {
          this.resolveSide = resolve
        }).then(res => {
          return res
        })
      },
      mainData: () => {
        return new Promise(resolve => {
          this.resolveMain = resolve
        }).then(res => {
          return res
        })
      },
      resolveMain: null,
      resolveSide: null
    }
  },
  created() {
    
    let param = {
      id: this.$route.params.id
    }
    alert(this.$route.params.id);
    getAction('/rec/modRecResult/queryById', param)
      .then(res => {
        let mData = []
        let sData = []
        if (res.success) {
          if (res.result.result) {
            mData.data = res.result.result.mainInfo
            sData.data = res.result.result.sideInfo
          }
        } else {
          this.$message.warning(res.message)
        }
        this.resolveMain(mData)
        this.resolveSide(sData)
      })
      .finally(() => {})
  },
  computed: {
    title() {
      return this.$route.meta.title
    }
  }
}
</script>

<style lang="less" scoped>
.title {
  color: rgba(0, 0, 0, 0.85);
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 16px;
}
</style>
