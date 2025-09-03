/* --------------------------------------------------------------------------------------
 * Name : AffcAaBasEbc.JAVA 
 * VER  : 1.0
 * PROJ : 교보생명 V3 Project
 * Copyright 교보생명보험주식회사 rights reserved.
 * 
 */
package kv3.lon.zz.ebc.rtalon.affcmgt.aamgt.affcaabasebi;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import devon.core.collection.LData;
import devon.core.collection.LMultiData;
import devon.core.exception.DevonException;
import devon.core.exception.LException;
import devon.core.exception.LBizException;
import devon.core.exception.LSysException;
import devon.core.log.LLog;
import devon.core.util.file.LFileUtil;
import devonframework.service.message.LMessage;
import devonframework.persistent.autodao.LCommonDao;
import devonframework.persistent.autodao.LMultiDao;
import devonframework.persistent.autodao.LAddBatchDao;
import devon.scp.service.nestedtx.NestedTx;
import kv3.core.util.MessageUtil;
import devon.scp.core.context.ContextHandler;
import devon.scp.service.paging.LPagingUtil;
import devon.core.collection.LPagingData;
import devon.scp.service.paging.vo.LIndexPagingVO;
import devon.scp.service.paging.vo.LScrollPagingVO;
import devon.scp.service.paging.vo.LRownumHandlingPagingVO;
import devon.scp.core.collection.util.LProtocolInitializeUtil;
import devon.scp.service.paging.LFPagingConstants;
import kv3.core.exception.BizException;
import kv3.core.exception.LInfMessageException;
import kv3.core.exception.LBizDuplicateException;
import kv3.core.exception.LBizExceptionMessage;
import kv3.core.exception.LBizNotAffectedException;
import kv3.core.exception.LBizNotFoundException;
import kv3.core.exception.LBizTooManyRowException;
import kv3.business.link.LServiceLink;
import devon.scp.service.paging.LPagingCommonDao;
import kv3.persistent.external.CommonSao;
import devon.scp.persistent.externalinterface.LCommonSao;
import kv3.util.KLDataConvertUtil;
import devon.core.exception.LDuplicateException;
import devonframework.persistent.autodao.LNotFoundException;
import devonframework.persistent.autodao.LTooManyRowException;
import kv3.common.syscomm.constants.*;
import kv3.util.KTypeConverter;
import kv3.util.KDBCryptUtil;
import kv3.util.KAuditColumnUtil;

/**
 * 
 *
 * @logicalName   사후연체기본Ebi
 * @version       1.0, 2024-06-13
 * @modelVersion  
 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::사후연체기본Ebi
 */
public class AffcAaBasEbc {
	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   개인고객색인[전화번호]목록조회
	 * @param LData iIdlCsIxTlnoListInq i개인고객색인전화번호목록조회
	 * @return LMultiData rIdlCsIxTlnoListInq r개인고객색인전화번호목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_개인고객색인[전화번호]목록조회
	 * 
	 */
	public LMultiData retvLstIdlCsIxByTlno(LData iIdlCsIxTlnoListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rIdlCsIxTlnoListInq = new LMultiData(); //# r개인고객색인전화번호목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		LData iIdlCsIxTlnoListInq_Enc = new LData();
		iIdlCsIxTlnoListInq_Enc = (LData) iIdlCsIxTlnoListInq.clone();
		KDBCryptUtil.encrypt(iIdlCsIxTlnoListInq_Enc, "tgkno:ENC_TEL_NO");
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstIdlCsIxByTlno",
				iIdlCsIxTlnoListInq_Enc); //##개인고객색인[전화번호]목록조회
		rIdlCsIxTlnoListInq = commonDao.executeQuery();
		LProtocolInitializeUtil.primitiveLMultiInitialize(rIdlCsIxTlnoListInq);
		return rIdlCsIxTlnoListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   공적구제수납목록조회
	 * @param LData iPbcRlifRcvListInq i공적구제수납목록조회
	 * @return LMultiData rPbcRlifRcvListInq r공적구제수납목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_공적구제수납목록조회
	 * 
	 */
	public LMultiData retvLstPbcRlifRcv(LData iPbcRlifRcvListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rPbcRlifRcvListInq = new LMultiData(); //# r공적구제수납목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstPbcRlifRcv",
				iPbcRlifRcvListInq); //##공적구제수납목록조회
		rPbcRlifRcvListInq = commonDao.executeQuery();
		LProtocolInitializeUtil.primitiveLMultiInitialize(rPbcRlifRcvListInq);
		return rPbcRlifRcvListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   보험수익자지급중지대상연체정보조회
	 * @param LData iIsBfrPnSopTgtAaImInq i보험수익자지급중지대상연체정보조회
	 * @return LData rIsBfrPnSopTgtAaImInq r보험수익자지급중지대상연체정보조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_보험수익자지급중지대상연체정보조회
	 * 
	 */
	public LData retvIsBfrPnSopTgtAaIm(LData iIsBfrPnSopTgtAaImInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LData rIsBfrPnSopTgtAaImInq = new LData(); //# r보험수익자지급중지대상연체정보조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvIsBfrPnSopTgtAaIm",
					iIsBfrPnSopTgtAaImInq); //##보험수익자지급중지대상연체정보조회
			rIsBfrPnSopTgtAaImInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rIsBfrPnSopTgtAaImInq);
		return rIsBfrPnSopTgtAaImInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[고객번호]목록조회
	 * @param LData iAffcAaBasCsNoListInq i사후연체기본고객번호목록조회
	 * @return LMultiData rAffcAaBasCsNoListInq r사후연체기본고객번호목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[고객번호]목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasByCsNo(LData iAffcAaBasCsNoListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasCsNoListInq = new LMultiData(); //# r사후연체기본고객번호목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasByCsNo",
				iAffcAaBasCsNoListInq); //##사후연체기본[고객번호]목록조회
		rAffcAaBasCsNoListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasCsNoListInq);
		return rAffcAaBasCsNoListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[누적연체일수]수정
	 * @param LData iAffcAaBasAccuAaDcnUpd i사후연체기본누적연체일수수정
	 * @return long rAffcAaBasAccuAaDcnUpd r사후연체기본누적연체일수수정 
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[누적연체일수]수정
	 * 
	 */
	public long uptAffcAaBasByAccuAaDcn(LData iAffcAaBasAccuAaDcnUpd)
			throws LException {
		//#Return 변수 선언 및 초기화
		long rAffcAaBasAccuAaDcnUpd = 0; //# r사후연체기본누적연체일수수정
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/uptAffcAaBasByAccuAaDcn",
				iAffcAaBasAccuAaDcnUpd); //##사후연체기본[누적연체일수]수정
		rAffcAaBasAccuAaDcnUpd = commonDao.executeUpdate();
		if (rAffcAaBasAccuAaDcnUpd == 0) {
			throw new LBizNotAffectedException(
					CommConst.MG_NOT_AFFECTED.getCode());
		}
		return rAffcAaBasAccuAaDcnUpd;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[대출번호]TCC연체상담정보조회
	 * @param LData iAffcAaBasLnNoTccAaCslImInq i사후연체기본대출번호TCC연체상담정보조회
	 * @return LData rAffcAaBasLnNoTccAaCslImInq r사후연체기본대출번호TCC연체상담정보조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[대출번호]TCC연체상담정보조회
	 * 
	 */
	public LData retvAffcAaBasByLnNo(LData iAffcAaBasLnNoTccAaCslImInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LData rAffcAaBasLnNoTccAaCslImInq = new LData(); //# r사후연체기본대출번호TCC연체상담정보조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvAffcAaBasByLnNo",
					iAffcAaBasLnNoTccAaCslImInq); //##사후연체기본[대출번호]TCC연체상담정보조회
			rAffcAaBasLnNoTccAaCslImInq = commonDao.executeQueryOnlySingle();
			KDBCryptUtil.decrypt(rAffcAaBasLnNoTccAaCslImInq,
					"aco_no:ENC_ACCT_NO", "oh_tlno:ENC_TEL_NO",
					"wrpl_tlno:ENC_TEL_NO", "calg_tlno:ENC_TEL_NO",
					"rgno:ENC_REG_NO", "oh_adr:ENC_ADDR", "wrpl_adr:ENC_ADDR",
					"etc_adr:ENC_ADDR");
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLnNoTccAaCslImInq);
		return rAffcAaBasLnNoTccAaCslImInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[대출번호]목록조회
	 * @param LData iAffcAaBasLnNoListInq i사후연체기본대출번호목록조회
	 * @return LMultiData rAffcAaBasLnNoListInq r사후연체기본대출번호목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[대출번호]목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasByLnNo(LData iAffcAaBasLnNoListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLnNoListInq = new LMultiData(); //# r사후연체기본대출번호목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/retvLstAffcAaBasByLnNo",
				iAffcAaBasLnNoListInq); //##사후연체기본[대출번호]목록조회
		rAffcAaBasLnNoListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLnNoListInq);
		return rAffcAaBasLnNoListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[연체해제]수정
	 * @param LData iAffcAaBasAaRelsUpd i사후연체기본연체해제수정
	 * @return long rAffcAaBasAaRelsUpd r사후연체기본연체해제수정 
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[연체해제]수정
	 * 
	 */
	public long uptAffcAaBasByAaRels(LData iAffcAaBasAaRelsUpd)
			throws LException {
		//#Return 변수 선언 및 초기화
		long rAffcAaBasAaRelsUpd = 0; //# r사후연체기본연체해제수정
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/uptAffcAaBasByAaRels",
				iAffcAaBasAaRelsUpd); //##사후연체기본[연체해제]수정
		rAffcAaBasAaRelsUpd = commonDao.executeUpdate();
		if (rAffcAaBasAaRelsUpd == 0) {
			throw new LBizNotAffectedException(
					CommConst.MG_NOT_AFFECTED.getCode());
		}
		return rAffcAaBasAaRelsUpd;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[원리금기장연체해제]수정
	 * @param LData iAffcAaBasPcitRgstAaRelsUpd i사후연체기본원리금기장연체해제수정
	 * @return long rAffcAaBasPcitRgstAaRelsUpd r사후연체기본원리금기장연체해제수정 
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[원리금기장연체해제]수정
	 * 
	 */
	public long uptAffcAaBasByPcitRgstAaRels(LData iAffcAaBasPcitRgstAaRelsUpd)
			throws LException {
		//#Return 변수 선언 및 초기화
		long rAffcAaBasPcitRgstAaRelsUpd = 0; //# r사후연체기본원리금기장연체해제수정
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/uptAffcAaBasByPcitRgstAaRels",
				iAffcAaBasPcitRgstAaRelsUpd); //##사후연체기본[원리금기장연체해제]수정
		rAffcAaBasPcitRgstAaRelsUpd = commonDao.executeUpdate();
		if (rAffcAaBasPcitRgstAaRelsUpd == 0) {
			throw new LBizNotAffectedException(
					CommConst.MG_NOT_AFFECTED.getCode());
		}
		return rAffcAaBasPcitRgstAaRelsUpd;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[최종SMS발송내역]수정
	 * @param LData iAffcAaBasLsSmsSndHisUpd i사후연체기본최종SMS발송내역수정
	 * @return long rAffcAaBasLsSmsSndHisUpd r사후연체기본최종SMS발송내역수정 
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[최종SMS발송내역]수정
	 * 
	 */
	public long uptAffcAaBasByLsSmsSndHis(LData iAffcAaBasLsSmsSndHisUpd)
			throws LException {
		//#Return 변수 선언 및 초기화
		long rAffcAaBasLsSmsSndHisUpd = 0; //# r사후연체기본최종SMS발송내역수정
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/uptAffcAaBasByLsSmsSndHis",
				iAffcAaBasLsSmsSndHisUpd); //##사후연체기본[최종SMS발송내역]수정
		rAffcAaBasLsSmsSndHisUpd = commonDao.executeUpdate();
		if (rAffcAaBasLsSmsSndHisUpd == 0) {
			throw new LBizNotAffectedException(
					CommConst.MG_NOT_AFFECTED.getCode());
		}
		return rAffcAaBasLsSmsSndHisUpd;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[최종상담내역]수정
	 * @param LData iAffcAaBasLsCslHisUpd i사후연체기본최종상담내역수정
	 * @return long rAffcAaBasLsCslHisUpd r사후연체기본최종상담내역수정 
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[최종상담내역]수정
	 * 
	 */
	public long uptAffcAaBasByLsCslHis(LData iAffcAaBasLsCslHisUpd)
			throws LException {
		//#Return 변수 선언 및 초기화
		long rAffcAaBasLsCslHisUpd = 0; //# r사후연체기본최종상담내역수정
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/uptAffcAaBasByLsCslHis",
				iAffcAaBasLsCslHisUpd); //##사후연체기본[최종상담내역]수정
		rAffcAaBasLsCslHisUpd = commonDao.executeUpdate();
		if (rAffcAaBasLsCslHisUpd == 0) {
			throw new LBizNotAffectedException(
					CommConst.MG_NOT_AFFECTED.getCode());
		}
		return rAffcAaBasLsCslHisUpd;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본[통합고객번호]목록조회
	 * @param LData iAffcAaBasIgCsNoListInq i사후연체기본통합고객번호목록조회
	 * @return LMultiData rAffcAaBasIgCsNoListInq r사후연체기본통합고객번호목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본[통합고객번호]목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasByIgCsNo(LData iAffcAaBasIgCsNoListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasIgCsNoListInq = new LMultiData(); //# r사후연체기본통합고객번호목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasByIgCsNo",
				iAffcAaBasIgCsNoListInq); //##사후연체기본[통합고객번호]목록조회
		rAffcAaBasIgCsNoListInq = commonDao.executeQuery();
		KDBCryptUtil.decrypt(rAffcAaBasIgCsNoListInq,
				"csg_bd_chp_tlno:ENC_TEL_NO");
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasIgCsNoListInq);
		return rAffcAaBasIgCsNoListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본대출종합정보목록조회
	 * @param LData iAffcAaBasLnGnlImListInq i사후연체기본대출종합정보목록조회
	 * @return LMultiData rAffcAaBasLnGnlImListInq r사후연체기본대출종합정보목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본대출종합정보목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasLnGnlIm(LData iAffcAaBasLnGnlImListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLnGnlImListInq = new LMultiData(); //# r사후연체기본대출종합정보목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LPagingCommonDao pagingDao;
		// Paging Parameter 처리
		LPagingUtil.setIndexPageInfo(iAffcAaBasLnGnlImListInq,
				iAffcAaBasLnGnlImListInq.getInt("page_no"),
				iAffcAaBasLnGnlImListInq.getInt("page_size"));
		pagingDao = new LPagingCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasLnGnlIm",
				iAffcAaBasLnGnlImListInq); //##사후연체기본대출종합정보목록조회
		rAffcAaBasLnGnlImListInq = pagingDao.executeQueryForIndexPage();
		KDBCryptUtil.decrypt(rAffcAaBasLnGnlImListInq, "aco_no:ENC_ACCT_NO");
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLnGnlImListInq);
		return rAffcAaBasLnGnlImListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본등록
	 * @param LData iAffcAaBasReg i사후연체기본등록
	 * @return long rAffcAaBasReg r사후연체기본등록 
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본등록
	 * 
	 */
	public long regtAffcAaBas(LData iAffcAaBasReg) throws LException {
		//#Return 변수 선언 및 초기화
		long rAffcAaBasReg = 0; //# r사후연체기본등록
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/regtAffcAaBas",
					iAffcAaBasReg); //##사후연체기본등록
			rAffcAaBasReg = commonDao.executeUpdate();
			if (rAffcAaBasReg == 0) {
				throw new LBizNotAffectedException(
						CommConst.MG_NOT_AFFECTED.getCode());
			}
		} catch (LDuplicateException e) {
			throw new LBizDuplicateException(CommConst.MG_DUPLICATE.getCode(),
					e);
		}
		return rAffcAaBasReg;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본락조회
	 * @param LData iAffcAaBasLckInq i사후연체기본락조회
	 * @return LData rAffcAaBasLckInq r사후연체기본락조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본락조회
	 * 
	 */
	public LData retvLckAffcAaBas(LData iAffcAaBasLckInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAffcAaBasLckInq = new LData(); //# r사후연체기본락조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/retvLckAffcAaBas",
					iAffcAaBasLckInq); //##사후연체기본락조회
			rAffcAaBasLckInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAffcAaBasLckInq);
		return rAffcAaBasLckInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본메시지목록조회
	 * @param LData iAffcAaBasMgListInq i사후연체기본메시지목록조회
	 * @return LMultiData rAffcAaBasMgListInq r사후연체기본메시지목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본메시지목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasMg(LData iAffcAaBasMgListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasMgListInq = new LMultiData(); //# r사후연체기본메시지목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasMg",
				iAffcAaBasMgListInq); //##사후연체기본메시지목록조회
		rAffcAaBasMgListInq = commonDao.executeQuery();
		KDBCryptUtil.decrypt(rAffcAaBasMgListInq, "tgkno:ENC_TEL_NO");
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAffcAaBasMgListInq);
		return rAffcAaBasMgListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본메시지페이징목록조회
	 * @param LData iAffcAaBasMgPagiListInq i사후연체기본메시지페이징목록조회
	 * @return LMultiData rAffcAaBasMgPagiListInq r사후연체기본메시지페이징목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본메시지페이징목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasMgPagi(LData iAffcAaBasMgPagiListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasMgPagiListInq = new LMultiData(); //# r사후연체기본메시지페이징목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LPagingCommonDao pagingDao;
		// Paging Parameter 처리
		LPagingUtil.setIndexPageInfo(iAffcAaBasMgPagiListInq,
				iAffcAaBasMgPagiListInq.getInt("page_no"),
				iAffcAaBasMgPagiListInq.getInt("page_size"));
		pagingDao = new LPagingCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasMgPagi",
				iAffcAaBasMgPagiListInq); //##사후연체기본메시지페이징목록조회
		rAffcAaBasMgPagiListInq = pagingDao.executeQueryForIndexPage();
		KDBCryptUtil.decrypt(rAffcAaBasMgPagiListInq, "tgkno:ENC_TEL_NO");
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasMgPagiListInq);
		return rAffcAaBasMgPagiListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본목록조회
	 * @param LData iAffcAaBasListInq i사후연체기본목록조회
	 * @return LMultiData rAffcAaBasListInq r사후연체기본목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBas(LData iAffcAaBasListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasListInq = new LMultiData(); //# r사후연체기본목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBas",
				iAffcAaBasListInq); //##사후연체기본목록조회
		rAffcAaBasListInq = commonDao.executeQuery();
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAffcAaBasListInq);
		return rAffcAaBasListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본법적조치가압류상세내용목록조회
	 * @param LData iAffcAaBasLeaMgmPrszDtlTxtListInq i사후연체기본법적조치가압류상세내용목록조회
	 * @return LMultiData rAffcAaBasLeaMgmPrszDtlTxtListInq r사후연체기본법적조치가압류상세내용목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본법적조치가압류상세내용목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasLeaMgmPrszDtlTxt(
			LData iAffcAaBasLeaMgmPrszDtlTxtListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLeaMgmPrszDtlTxtListInq = new LMultiData(); //# r사후연체기본법적조치가압류상세내용목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasLeaMgmPrszDtlTxt",
				iAffcAaBasLeaMgmPrszDtlTxtListInq); //##사후연체기본법적조치가압류상세내용목록조회
		rAffcAaBasLeaMgmPrszDtlTxtListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLeaMgmPrszDtlTxtListInq);
		return rAffcAaBasLeaMgmPrszDtlTxtListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본법적조치경매상세내용목록조회
	 * @param LData iAffcAaBasLeaMgmAucDtlTxtListInq i사후연체기본법적조치경매상세내용목록조회
	 * @return LMultiData rAffcAaBasLeaMgmAucDtlTxtListInq r사후연체기본법적조치경매상세내용목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본법적조치경매상세내용목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasLeaMgmAucDtlTxt(
			LData iAffcAaBasLeaMgmAucDtlTxtListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLeaMgmAucDtlTxtListInq = new LMultiData(); //# r사후연체기본법적조치경매상세내용목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasLeaMgmAucDtlTxt",
				iAffcAaBasLeaMgmAucDtlTxtListInq); //##사후연체기본법적조치경매상세내용목록조회
		rAffcAaBasLeaMgmAucDtlTxtListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLeaMgmAucDtlTxtListInq);
		return rAffcAaBasLeaMgmAucDtlTxtListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본법적조치관리총괄현황목록조회
	 * @param LData iAffcAaBasLeaMgmMgtGnrPrcnListInq i사후연체기본법적조치관리총괄현황목록조회
	 * @return LMultiData rAffcAaBasLeaMgmMgtGnrPrcnListInq r사후연체기본법적조치관리총괄현황목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본법적조치관리총괄현황목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasLeaMgmMgtGnrPrcn(
			LData iAffcAaBasLeaMgmMgtGnrPrcnListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLeaMgmMgtGnrPrcnListInq = new LMultiData(); //# r사후연체기본법적조치관리총괄현황목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasLeaMgmMgtGnrPrcn",
				iAffcAaBasLeaMgmMgtGnrPrcnListInq); //##사후연체기본법적조치관리총괄현황목록조회
		rAffcAaBasLeaMgmMgtGnrPrcnListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLeaMgmMgtGnrPrcnListInq);
		return rAffcAaBasLeaMgmMgtGnrPrcnListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본법적조치소송상세내용목록조회
	 * @param LData iAffcAaBasLeaMgmLsuDtlTxtListInq i사후연체기본법적조치소송상세내용목록조회
	 * @return LMultiData rAffcAaBasLeaMgmLsuDtlTxtListInq r사후연체기본법적조치소송상세내용목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본법적조치소송상세내용목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasLeaMgmLsuDtlTxt(
			LData iAffcAaBasLeaMgmLsuDtlTxtListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLeaMgmLsuDtlTxtListInq = new LMultiData(); //# r사후연체기본법적조치소송상세내용목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasLeaMgmLsuDtlTxt",
				iAffcAaBasLeaMgmLsuDtlTxtListInq); //##사후연체기본법적조치소송상세내용목록조회
		rAffcAaBasLeaMgmLsuDtlTxtListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLeaMgmLsuDtlTxtListInq);
		return rAffcAaBasLeaMgmLsuDtlTxtListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본법적조치위탁채권[마감일자]조회
	 * @param LData iAffcAaBasLeaMgmCsgBdClsgDtInq i사후연체기본법적조치위탁채권마감일자조회
	 * @return LData rAffcAaBasLeaMgmCsgBdClsgDtInq r사후연체기본법적조치위탁채권마감일자조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본법적조치위탁채권[마감일자]조회
	 * 
	 */
	public LData retvAffcAaBasLeaMgmCsgBdByClsgDt(
			LData iAffcAaBasLeaMgmCsgBdClsgDtInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAffcAaBasLeaMgmCsgBdClsgDtInq = new LData(); //# r사후연체기본법적조치위탁채권마감일자조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvAffcAaBasLeaMgmCsgBdByClsgDt",
					iAffcAaBasLeaMgmCsgBdClsgDtInq); //##사후연체기본법적조치위탁채권[마감일자]조회
			rAffcAaBasLeaMgmCsgBdClsgDtInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLeaMgmCsgBdClsgDtInq);
		return rAffcAaBasLeaMgmCsgBdClsgDtInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본법적조치진행상세내역[조회구분]목록조회
	 * @param LData iAffcAaBasLeaMgmPrsDtlHisInqDvListInq i사후연체기본법적조치진행상세내역조회구분목록조회
	 * @return LMultiData rAffcAaBasLeaMgmPrsDtlHisInqDvListInq r사후연체기본법적조치진행상세내역조회구분목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본법적조치진행상세내역[조회구분]목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasLeaMgmPrsDtlHisByInqDv(
			LData iAffcAaBasLeaMgmPrsDtlHisInqDvListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLeaMgmPrsDtlHisInqDvListInq = new LMultiData(); //# r사후연체기본법적조치진행상세내역조회구분목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LPagingCommonDao pagingDao;
		// Paging Parameter 처리
		LPagingUtil.setIndexPageInfo(iAffcAaBasLeaMgmPrsDtlHisInqDvListInq,
				iAffcAaBasLeaMgmPrsDtlHisInqDvListInq.getInt("page_no"),
				iAffcAaBasLeaMgmPrsDtlHisInqDvListInq.getInt("page_size"));
		pagingDao = new LPagingCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasLeaMgmPrsDtlHisByInqDv",
				iAffcAaBasLeaMgmPrsDtlHisInqDvListInq); //##사후연체기본법적조치진행상세내역[조회구분]목록조회
		rAffcAaBasLeaMgmPrsDtlHisInqDvListInq = pagingDao
				.executeQueryForIndexPage();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLeaMgmPrsDtlHisInqDvListInq);
		return rAffcAaBasLeaMgmPrsDtlHisInqDvListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본법적조치진행상세내역목록조회
	 * @param LData iAffcAaBasLeaMgmPrsDtlHisListInq i사후연체기본법적조치진행상세내역목록조회
	 * @return LMultiData rAffcAaBasLeaMgmPrsDtlHisListInq r사후연체기본법적조치진행상세내역목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본법적조치진행상세내역목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasLeaMgmPrsDtlHis(
			LData iAffcAaBasLeaMgmPrsDtlHisListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLeaMgmPrsDtlHisListInq = new LMultiData(); //# r사후연체기본법적조치진행상세내역목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LPagingCommonDao pagingDao;
		// Paging Parameter 처리
		LPagingUtil.setIndexPageInfo(iAffcAaBasLeaMgmPrsDtlHisListInq,
				iAffcAaBasLeaMgmPrsDtlHisListInq.getInt("page_no"),
				iAffcAaBasLeaMgmPrsDtlHisListInq.getInt("page_size"));
		pagingDao = new LPagingCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasLeaMgmPrsDtlHis",
				iAffcAaBasLeaMgmPrsDtlHisListInq); //##사후연체기본법적조치진행상세내역목록조회
		rAffcAaBasLeaMgmPrsDtlHisListInq = pagingDao.executeQueryForIndexPage();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLeaMgmPrsDtlHisListInq);
		return rAffcAaBasLeaMgmPrsDtlHisListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본법적조치현황목록조회
	 * @param LData iAffcAaBasLeaMgmPrcnListInq i사후연체기본법적조치현황목록조회
	 * @return LMultiData rAffcAaBasLeaMgmPrcnListInq r사후연체기본법적조치현황목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본법적조치현황목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasLeaMgmPrcn(
			LData iAffcAaBasLeaMgmPrcnListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasLeaMgmPrcnListInq = new LMultiData(); //# r사후연체기본법적조치현황목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasLeaMgmPrcn",
				iAffcAaBasLeaMgmPrcnListInq); //##사후연체기본법적조치현황목록조회
		rAffcAaBasLeaMgmPrcnListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasLeaMgmPrcnListInq);
		return rAffcAaBasLeaMgmPrcnListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본사고등록총건수목록조회
	 * @param LData iAffcAaBasAccRegTcnListInq i사후연체기본사고등록총건수목록조회
	 * @return LMultiData rAffcAaBasAccRegTcnListInq r사후연체기본사고등록총건수목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본사고등록총건수목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasAccRegTcn(LData iAffcAaBasAccRegTcnListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasAccRegTcnListInq = new LMultiData(); //# r사후연체기본사고등록총건수목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasAccRegTcn",
				iAffcAaBasAccRegTcnListInq); //##사후연체기본사고등록총건수목록조회
		rAffcAaBasAccRegTcnListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasAccRegTcnListInq);
		return rAffcAaBasAccRegTcnListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본사고등록현황목록조회
	 * @param LData iAffcAaBasAccRegPrcnListInq i사후연체기본사고등록현황목록조회
	 * @return LMultiData rAffcAaBasAccRegPrcnListInq r사후연체기본사고등록현황목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본사고등록현황목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasAccRegPrcn(
			LData iAffcAaBasAccRegPrcnListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasAccRegPrcnListInq = new LMultiData(); //# r사후연체기본사고등록현황목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LPagingCommonDao pagingDao;
		// Paging Parameter 처리
		LPagingUtil.setIndexPageInfo(iAffcAaBasAccRegPrcnListInq,
				iAffcAaBasAccRegPrcnListInq.getInt("page_no"),
				iAffcAaBasAccRegPrcnListInq.getInt("page_size"));
		pagingDao = new LPagingCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasAccRegPrcn",
				iAffcAaBasAccRegPrcnListInq); //##사후연체기본사고등록현황목록조회
		rAffcAaBasAccRegPrcnListInq = pagingDao.executeQueryForIndexPage();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasAccRegPrcnListInq);
		return rAffcAaBasAccRegPrcnListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본수정
	 * @param LData iAffcAaBasUpd i사후연체기본수정
	 * @return long rAffcAaBasUpd r사후연체기본수정 
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본수정
	 * 
	 */
	public long uptAffcAaBas(LData iAffcAaBasUpd) throws LException {
		//#Return 변수 선언 및 초기화
		long rAffcAaBasUpd = 0; //# r사후연체기본수정
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/uptAffcAaBas",
				iAffcAaBasUpd); //##사후연체기본수정
		rAffcAaBasUpd = commonDao.executeUpdate();
		if (rAffcAaBasUpd == 0) {
			throw new LBizNotAffectedException(
					CommConst.MG_NOT_AFFECTED.getCode());
		}
		return rAffcAaBasUpd;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본연체여부조회
	 * @param LData iAffcAaBasAaYnInq i사후연체기본연체여부조회
	 * @return LData rAffcAaBasAaYnInq r사후연체기본연체여부조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본연체여부조회
	 * 
	 */
	public LData retvAffcAaBasAaYn(LData iAffcAaBasAaYnInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAffcAaBasAaYnInq = new LData(); //# r사후연체기본연체여부조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/retvAffcAaBasAaYn",
					iAffcAaBasAaYnInq); //##사후연체기본연체여부조회
			rAffcAaBasAaYnInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAffcAaBasAaYnInq);
		return rAffcAaBasAaYnInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본연체위탁현황목록조회
	 * @param LData iAffcAaBasAaCsgPrcnListInq i사후연체기본연체위탁현황목록조회
	 * @return LMultiData rAffcAaBasAaCsgPrcnListInq r사후연체기본연체위탁현황목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본연체위탁현황목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasAaCsgPrcn(LData iAffcAaBasAaCsgPrcnListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasAaCsgPrcnListInq = new LMultiData(); //# r사후연체기본연체위탁현황목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LPagingCommonDao pagingDao;
		// Paging Parameter 처리
		LPagingUtil.setIndexPageInfo(iAffcAaBasAaCsgPrcnListInq,
				iAffcAaBasAaCsgPrcnListInq.getInt("page_no"),
				iAffcAaBasAaCsgPrcnListInq.getInt("page_size"));
		pagingDao = new LPagingCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasAaCsgPrcn",
				iAffcAaBasAaCsgPrcnListInq); //##사후연체기본연체위탁현황목록조회
		rAffcAaBasAaCsgPrcnListInq = pagingDao.executeQueryForIndexPage();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasAaCsgPrcnListInq);
		return rAffcAaBasAaCsgPrcnListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본연체이력조회
	 * @param LData iAffcAaBasAaHsInq i사후연체기본연체이력조회
	 * @return LData rAffcAaBasAaHsInq r사후연체기본연체이력조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본연체이력조회
	 * 
	 */
	public LData retvAffcAaBasAaHs(LData iAffcAaBasAaHsInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAffcAaBasAaHsInq = new LData(); //# r사후연체기본연체이력조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvAffcAaBasAaHs",
					iAffcAaBasAaHsInq); //##사후연체기본연체이력조회
			rAffcAaBasAaHsInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAffcAaBasAaHsInq);
		return rAffcAaBasAaHsInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본연체통계[통합고객번호]조회
	 * @param LData iAffcAaBasAaStaIgCsNoInq i사후연체기본연체통계통합고객번호조회
	 * @return LData rAffcAaBasAaStaIgCsNoInq r사후연체기본연체통계통합고객번호조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본연체통계[통합고객번호]조회
	 * 
	 */
	public LData retvAffcAaBasAaStaByIgCsNo(LData iAffcAaBasAaStaIgCsNoInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LData rAffcAaBasAaStaIgCsNoInq = new LData(); //# r사후연체기본연체통계통합고객번호조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvAffcAaBasAaStaByIgCsNo",
					iAffcAaBasAaStaIgCsNoInq); //##사후연체기본연체통계[통합고객번호]조회
			rAffcAaBasAaStaIgCsNoInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasAaStaIgCsNoInq);
		return rAffcAaBasAaStaIgCsNoInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본연체통보일자목록조회
	 * @param LData iAffcAaBasAaDspcDtListInq i사후연체기본연체통보일자목록조회
	 * @return LMultiData rAffcAaBasAaDspcDtListInq r사후연체기본연체통보일자목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본연체통보일자목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasAaDspcDt(LData iAffcAaBasAaDspcDtListInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasAaDspcDtListInq = new LMultiData(); //# r사후연체기본연체통보일자목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasAaDspcDt",
				iAffcAaBasAaDspcDtListInq); //##사후연체기본연체통보일자목록조회
		rAffcAaBasAaDspcDtListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasAaDspcDtListInq);
		return rAffcAaBasAaDspcDtListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본위탁사유별현황목록조회
	 * @param LData iAffcAaBasCsgBrsnPrcnListInq i사후연체기본위탁사유별현황목록조회
	 * @return LMultiData rAffcAaBasCsgBrsnPrcnListInq r사후연체기본위탁사유별현황목록조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본위탁사유별현황목록조회
	 * 
	 */
	public LMultiData retvLstAffcAaBasCsgBrsnPrcn(
			LData iAffcAaBasCsgBrsnPrcnListInq) throws LException {
		//#Return 변수 선언 및 초기화
		LMultiData rAffcAaBasCsgBrsnPrcnListInq = new LMultiData(); //# r사후연체기본위탁사유별현황목록조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvLstAffcAaBasCsgBrsnPrcn",
				iAffcAaBasCsgBrsnPrcnListInq); //##사후연체기본위탁사유별현황목록조회
		rAffcAaBasCsgBrsnPrcnListInq = commonDao.executeQuery();
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(rAffcAaBasCsgBrsnPrcnListInq);
		return rAffcAaBasCsgBrsnPrcnListInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본장기연체정보조회
	 * @param LData iAffcAaBasLtmAaImInq i사후연체기본장기연체정보조회
	 * @return LData rAffcAaBasLtmAaImInq r사후연체기본장기연체정보조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본장기연체정보조회
	 * 
	 */
	public LData retvAffcAaBasLtmAaIm(LData iAffcAaBasLtmAaImInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LData rAffcAaBasLtmAaImInq = new LData(); //# r사후연체기본장기연체정보조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvAffcAaBasLtmAaIm",
					iAffcAaBasLtmAaImInq); //##사후연체기본장기연체정보조회
			rAffcAaBasLtmAaImInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAffcAaBasLtmAaImInq);
		return rAffcAaBasLtmAaImInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본전세자금대출수정
	 * @param LData iAffcAaBasLsdbCapLn i사후연체기본전세자금대출
	 * @return long rAffcAaBasLsdbCapLn r사후연체기본전세자금대출 
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본전세자금대출수정
	 * 
	 */
	public long uptAffcAaBasLsdbCapLn(LData iAffcAaBasLsdbCapLn)
			throws LException {
		//#Return 변수 선언 및 초기화
		long rAffcAaBasLsdbCapLn = 0; //# r사후연체기본전세자금대출
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		commonDao = new LCommonDao(
				"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/uptAffcAaBasLsdbCapLn",
				iAffcAaBasLsdbCapLn); //##사후연체기본전세자금대출수정
		rAffcAaBasLsdbCapLn = commonDao.executeUpdate();
		if (rAffcAaBasLsdbCapLn == 0) {
			throw new LBizNotAffectedException(
					CommConst.MG_NOT_AFFECTED.getCode());
		}
		return rAffcAaBasLsdbCapLn;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   사후연체기본조회
	 * @param LData iAffcAaBasInq i사후연체기본조회
	 * @return LData rAffcAaBasInq r사후연체기본조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_사후연체기본조회
	 * 
	 */
	public LData retvAffcAaBas(LData iAffcAaBasInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAffcAaBasInq = new LData(); //# r사후연체기본조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/TB_AMA1001/retvAffcAaBas",
					iAffcAaBasInq); //##사후연체기본조회
			rAffcAaBasInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAffcAaBasInq);
		return rAffcAaBasInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   성과평가관리대상여부조회
	 * @param LData iRstEvlMgtTgtYnInq i성과평가관리대상여부조회
	 * @return LData rRstEvlMgtTgtYnInq r성과평가관리대상여부조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_성과평가관리대상여부조회
	 * 
	 */
	public LData retvRstEvlMgtTgtYn(LData iRstEvlMgtTgtYnInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rRstEvlMgtTgtYnInq = new LData(); //# r성과평가관리대상여부조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvRstEvlMgtTgtYn",
					iRstEvlMgtTgtYnInq); //##성과평가관리대상여부조회
			rRstEvlMgtTgtYnInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rRstEvlMgtTgtYnInq);
		return rRstEvlMgtTgtYnInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   연체대출사후관리정보조회
	 * @param LData iAaLnAffcMgtImInq i연체대출사후관리정보조회
	 * @return LData rAaLnAffcMgtImInq r연체대출사후관리정보조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_연체대출사후관리정보조회
	 * 
	 */
	public LData retvAaLnAffcMgtIm(LData iAaLnAffcMgtImInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAaLnAffcMgtImInq = new LData(); //# r연체대출사후관리정보조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvAaLnAffcMgtIm",
					iAaLnAffcMgtImInq); //##연체대출사후관리정보조회
			rAaLnAffcMgtImInq = commonDao.executeQueryOnlySingle();
			KDBCryptUtil.decrypt(rAaLnAffcMgtImInq, "tgkno:ENC_TEL_NO");
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAaLnAffcMgtImInq);
		return rAaLnAffcMgtImInq;
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     
	 * @logicalName   자동심사기준연체정보조회
	 * @param LData iAutJgBsAaImInq i자동심사기준연체정보조회
	 * @return LData rAutJgBsAaImInq r자동심사기준연체정보조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_ZZ(여신공통)
	 * @fullPath      2.시스템명세모델::05.엔터티컴포넌트::소매여신::사후관리::연체관리::사후연체기본Ebi::CORA_사후연체기본Ebi::ACSD_자동심사기준연체정보조회
	 * 
	 */
	public LData retvAutJgBsAaIm(LData iAutJgBsAaImInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAutJgBsAaImInq = new LData(); //# r자동심사기준연체정보조회
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LCommonDao commonDao;
		try {
			commonDao = new LCommonDao(
					"lon/zz/rtalon/affcmgt/aamgt/affcaabasebi/AffcAaBasJDao/retvAutJgBsAaIm",
					iAutJgBsAaImInq); //##자동심사기준연체정보조회
			rAutJgBsAaImInq = commonDao.executeQueryOnlySingle();
		} catch (LNotFoundException e) {
			throw new LBizNotFoundException(CommConst.MG_NOT_FOUND.getCode(), e);
		} catch (LTooManyRowException e) {
			throw new LBizTooManyRowException(
					CommConst.MG_TOO_MANY_ROW.getCode(), e);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAutJgBsAaImInq);
		return rAutJgBsAaImInq;
	}
}