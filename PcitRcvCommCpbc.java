/* --------------------------------------------------------------------------------------
 * Name : PcitRcvCommCpbc.JAVA 
 * VER  : 1.0
 * PROJ : 교보생명 V3 Project
 * Copyright 교보생명보험주식회사 rights reserved.
 * 
 */
package kv3.lon.rl.cpbc.rcvmgt.pcitrcvcommcpbi;

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
import kv3.lon.rl.constants.BdDvCdConst;
import kv3.lon.rl.constants.BrafDvCdConst;
import kv3.lon.rl.constants.LonAuthMgtCdConst;
import kv3.zzz.constants.CommYnEynConst;
import kv3.common.syscomm.constants.MaskDvConst;
import kv3.zzz.constants.DawRreNoDvCdConst;
import kv3.zzz.constants.IdlGpDvCdConst;
import kv3.zzz.constants.RlnmCfNoKdCdConst;
import kv3.lon.rl.constants.LonTlnoConst;
import kv3.zzz.constants.EmalTxtFormDvCdConst;
import kv3.zzz.constants.GeSndTgtDvCdConst;
import kv3.zzz.constants.SndTrpnDvCdConst;

import kv3.util.KDateUtil;
import devon.core.log.LLog;
import kv3.util.KStringUtil;
import kv3.util.KContextUtil;
import kv3.util.KTypeConverter;
import kv3.util.KIntegrationCodeUtil;
import kv3.util.KExceptionUtil;
import kv3.util.KThreadMngUtil;
import kv3.util.KBizDateUtil;
import kv3.util.KCommMngUtil;
import kv3.bizcom.util.BJsonConvertUtil;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrbas.loncnrbasrcvpatebi.LonCnrBasRcvPatEbc;
import kv3.lon.zz.ebc.rtalon.londln.rcvhis.lonrcvhisebi.LonRcvHisEbc;
import kv3.zzz.cpbc.auth.authmgtcpbi.AuthMgtCpbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrbas.loncnrbasebi.LonCnrBasEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrbas.lonauttrsbasebi.LonAutTrsBasEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrbas.londfmagtbdeprchisebi.LonDfmAgtBdePrcHisEbc;
import kv3.lon.zz.ebc.rtalon.londln.rcvhis.lonrcvbrafdtlebi.LonRcvBrafDtlEbc;
import kv3.lon.zz.ebc.rtalon.londln.rcvhis.lonrcvcnlhisebi.LonRcvCnlHisEbc;
import kv3.lon.zz.ebc.rtalon.londln.lnpn.lonlndlnhisebi.LonLnDlnHisEbc;
import kv3.lon.zz.ebc.rtalon.londln.spcbd.spcbdrcnlhisebi.SpcBdRcnlHisEbc;
import kv3.lon.zz.ebc.rtalon.affcmgt.aaspt.spcbdbasebi.SpcBdBasEbc;
import kv3.lon.zz.ebc.rtalon.londln.spcbd.spcbdrcnlbrafdtlebi.SpcBdRcnlBrafDtlEbc;
import kv3.zzz.ebc.orz.orzbasebi.OrzBasEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrmgt.lonrcvimpihisebi.LonRcvImpiHisEbc;
import kv3.lon.zz.ebc.rtalon.londln.acnyprc.lonwickrdmyclsgebi.LonWickRdmyClsgEbc;
import kv3.lon.zz.ebc.rtalon.londln.trsmgt.lonbdetrshisebi.LonBdeTrsHisEbc;
import kv3.lon.zz.ebc.rtalon.londln.trsmgt.lonimdttrshisebi.LonImdtTrsHisEbc;
import kv3.lon.zz.ebc.rtalon.londln.trsmgt.lonrsvtrshisebi.LonRsvTrsHisEbc;
import kv3.lon.zz.ebc.rtalon.loncsl.loncsl.exlnlnhisebi.ExlnLnHisEbc;
import kv3.lon.zz.ebc.rtalon.londln.rcvhis.crpcondchrcvreqhisebi.CrpCondChRcvReqHisEbc;
import kv3.common.ibc.arp.VtlAcoMgtIbc;
import kv3.zzz.ebc.orz.ombasebi.OmBasEbc;
import kv3.common.ibc.cim.CsCommImIbc;
import kv3.common.ibc.cim.IdlCsIbc;
import kv3.common.ibc.cim.GpCsIbc;
import kv3.lon.rl.cpbc.loncomm.idqcsmgtcpbi.IdqCsMgtCpbc;
import kv3.lon.zz.ebc.rtalon.londln.rcvhis.lonpcitclctempebi.LonPcitClcTempEbc;
import kv3.common.ibc.arp.ImdtTrsIbc;
import kv3.lon.zz.ebc.rtalon.affcmgt.aamgt.affcaabasebi.AffcAaBasEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrmgt.rttorzolndetsebi.RttOrzOlnDetsEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrbas.lonhsecapmgthisebi.LonHseCapMgtHisEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrmgt.prsdwrkomhisebi.PrsdWrkOmHisEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrmgt.idlretiacosptempmgtbsebi.IdlRetiAcoSptEmpMgtBsEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrmgt.esoptgtldghisebi.EsopTgtLdgHisEbc;
import kv3.lon.zz.ebc.rtalon.lonjg.lonrcpcn.hsescyselctrlhisebi.HseScySelCtrlHisEbc;
import kv3.lon.zz.ebc.rtalon.affcmgt.crp.debmetprshisebi.DebMetPrsHisEbc;
import kv3.lon.zz.ebc.rtalon.lonjg.lonrcpcn.lonrcpbasebi.LonRcpBasEbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrbas.lonirtpstrpyprghisebi.LonIrtPstRpyPrgHisEbc;
import kv3.lon.rl.cpbc.loncomm.lonacnyorzinqcpbi.LonAcnyOrzInqCpbc;
import kv3.lon.zz.ebc.rtalon.londln.rcvhis.rpyinqymgthisebi.RpyInqyMgtHisEbc;
import kv3.common.ibc.inm.IgGeMgtIbc;
import kv3.common.ibc.slm.HmreMgtIbc;
import kv3.lon.zz.ebc.rtalon.loncnr.loncnrbas.ittchgophisebi.IttChgOpHisEbc;
import kv3.lon.zz.ebc.rtalon.londln.rcvhis.loninatocchisebi.LonInatOccHisEbc;

/**
 * 
 *
 * @logicalName   원리금수납공통Cpbi
 * @version       1.0, 2025-06-18
 * @modelVersion  
 * @modelProject  KV3_MDL_LON_RL(소매여신)
 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::원리금수납공통Cpbi
 */
public class PcitRcvCommCpbc {
	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055117
	 * @logicalName   거치후할부변경조건산출
	 * @param LData iAfdfInslChCondCu i거치후할부변경조건산출
	 * @return LData rAfdfInslChCondCu r거치후할부변경조건산출
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_거치후할부변경조건산출
	 * 
	 */
	public LData cmptAfdfInslChCond(LData iAfdfInslChCondCu) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAfdfInslChCondCu = new LData(); //# r거치후할부변경조건산출
												//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iLonCnrBasLnNoInq = new LData(); //# i여신계약기본대출번호조회
		LData rLonCnrBasLnNoInq = new LData(); //# r여신계약기본대출번호조회
		LData iMonEmthDtCfCh = new LData(); //# i월월말일자확인변경
		LData rMonEmthDtCfCh = new LData(); //# r월월말일자확인변경
											//#호출 컴포넌트 초기화
		LonCnrBasRcvPatEbc lonCnrBasRcvPatEbc = new LonCnrBasRcvPatEbc(); //# 여신계약기본수납파트Ebi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수설정
		// =============================================================================
		long lDfrmMcn = 0; //#l거치월수
		String sPrsYn = "Y"; //#s진행여부

		String sOpDt = "00010101"; //#s작업일자
		String sOpDay = ""; //#s작업일
		String sOpDtK = "00010101"; //#s작업일자K
		String sOpDayK = ""; //#s작업일K
		String sDfrmPdEdDt = "00010101"; //#s거치기간종료일자

		String sPstPdEdDt = "00010101"; //#s유예기간종료일자

		long lLnPdTtim = 0; //#l대출기간총회차

		// =============================================================================
		// ######### GeneralCodeBlock ##여신계약기본[대출번호]조회  입력값 세팅
		// =============================================================================
		// [i여신계약기본대출번호조회]  
		iLonCnrBasLnNoInq = new LData();
		LProtocolInitializeUtil.primitiveLMultiInitialize(iLonCnrBasLnNoInq);

		iLonCnrBasLnNoInq.setString("ln_no",
				iAfdfInslChCondCu.getString("ln_no"));
		LProtocolInitializeUtil.primitiveLMultiInitialize(iLonCnrBasLnNoInq);
		rLonCnrBasLnNoInq = lonCnrBasRcvPatEbc
				.retvLonCnrBasByLnNo(iLonCnrBasLnNoInq); //##여신계약기본[대출번호]조회

		// =============================================================================
		// ######### GeneralCodeBlock ##대출기간 총회차 구하기
		// =============================================================================
		lLnPdTtim = KDateUtil.getMonthInterval(
				rLonCnrBasLnNoInq.getString("ln_dt"),
				rLonCnrBasLnNoInq.getString("exp_dt"), 1) + 1;
		while (lDfrmMcn < lLnPdTtim
				&& (KLDataConvertUtil.equals(
						rLonCnrBasLnNoInq.getString("rpy_mth_cd"), "03") || KLDataConvertUtil
						.equals(rLonCnrBasLnNoInq.getString("rpy_mth_cd"), "05"))
				&& KLDataConvertUtil.equals(sPrsYn, "Y")) {

			// =============================================================================
			// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅
			// =============================================================================
			// [i월월말일자확인변경]

			iMonEmthDtCfCh.setString("in_dt",
					rLonCnrBasLnNoInq.getString("nt_rpy_dt"));

			iMonEmthDtCfCh.setLong("mn_cnt",
					iAfdfInslChCondCu.getLong("pst_pd"));
			LProtocolInitializeUtil.primitiveLMultiInitialize(iMonEmthDtCfCh);
			rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

			// =============================================================================
			// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑
			// =============================================================================
			// [r월월말일자확인변경]

			sPstPdEdDt = rMonEmthDtCfCh.getString("otpt_dt");

			// =============================================================================
			// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅
			// =============================================================================
			// [i월월말일자확인변경]

			iMonEmthDtCfCh = new LData();
			LProtocolInitializeUtil.primitiveLMultiInitialize(iMonEmthDtCfCh);

			lDfrmMcn = lDfrmMcn + 1;

			iMonEmthDtCfCh.setString("in_dt",
					rLonCnrBasLnNoInq.getString("ln_dt"));
			iMonEmthDtCfCh.setLong("mn_cnt", lDfrmMcn);
			LProtocolInitializeUtil.primitiveLMultiInitialize(iMonEmthDtCfCh);
			rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

			// =============================================================================
			// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑 - 전역변수 거치기간종료일자
			// =============================================================================
			sDfrmPdEdDt = rMonEmthDtCfCh.getString("otpt_dt");

			// =============================================================================
			// ######### GeneralCodeBlock ##전역변수.작업일자=납입응당일자, 작업일자K=거치기간종료일자 세팅
			// =============================================================================
			sOpDt = rLonCnrBasLnNoInq.getString("pa_fsr_dt");
			sOpDtK = sDfrmPdEdDt;

			//LLog.debug.println("■■■■■ 100_작업일자  : " + [s작업일자]);
			//LLog.debug.println("■■■■■ 100_작업일자K : " + [s작업일자K]);
			if (sOpDt.compareTo(sDfrmPdEdDt) <= 0
					|| KLDataConvertUtil.equals(
							KStringUtil.substring(sOpDtK, 0, 6),
							KStringUtil.substring(sOpDt, 0, 6))) {
				while (sOpDt.compareTo(sDfrmPdEdDt) < 0) {

					// =============================================================================
					// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅
					// =============================================================================
					// [i월월말일자확인변경]  

					iMonEmthDtCfCh.setString("in_dt", sOpDt);
					iMonEmthDtCfCh.setLong("mn_cnt", 1);
					LProtocolInitializeUtil
							.primitiveLMultiInitialize(iMonEmthDtCfCh);
					rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

					// =============================================================================
					// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑
					// =============================================================================
					sOpDt = rMonEmthDtCfCh.getString("otpt_dt");
					//LLog.debug.println("■■■■■ 101_작업일자  : " + [s작업일자]);
				}

				// =============================================================================
				// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅
				// =============================================================================
				// [i월월말일자확인변경]  

				iMonEmthDtCfCh.setString("in_dt", sOpDt);
				iMonEmthDtCfCh.setLong("mn_cnt", 1);
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iMonEmthDtCfCh);
				rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

				// =============================================================================
				// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑
				// =============================================================================
				// [r월월말일자확인변경]

				sOpDt = rMonEmthDtCfCh.getString("otpt_dt");

				if (KLDataConvertUtil.equals(sOpDt, sPstPdEdDt)) {
					sPrsYn = "N";
					rAfdfInslChCondCu.setLong("dfrm_mcn", lDfrmMcn);
					rAfdfInslChCondCu
							.setLong("insl_ttim", lLnPdTtim - lDfrmMcn);
					rAfdfInslChCondCu.setLong("insl_pa_tim", 0);
					rAfdfInslChCondCu.setString("nt_rpy_day", sPstPdEdDt);
				}
				//LLog.debug.println("■■■■■ 102_작업일자  : " + [s작업일자]);
			} else {

				// =============================================================================
				// ######### GeneralCodeBlock ##작업일수
				// =============================================================================
				sOpDayK = KStringUtil.substring(sOpDtK, 6, 8);
				sOpDay = KStringUtil.substring(sOpDt, 6, 8);

				//LLog.debug.println("■■■■■ 103_작업일자  : " + [s작업일자]);
				//LLog.debug.println("■■■■■ 103_작업일자K : " + [s작업일자K]);
				//LLog.debug.println("■■■■■ 103_작업일    : " + [s작업일]);
				//LLog.debug.println("■■■■■ 103_작업일K   : " + [s작업일K]);
				if (sOpDayK.compareTo(sOpDay) > 0) {

					// =============================================================================
					// ######### GeneralCodeBlock ##거치후 최초 납입응당일자 산출 - 전역변수.작업일자 = 전역변수.거치기간종료일자 ~ 전역변수.작업일자 일수 년월일 산출
					// =============================================================================
					String sStDt = sDfrmPdEdDt; //#s시작일자
					String sEdDt = sOpDt; //#s종료일자
					int iDiffMnCnt = 0; //#i차이개월수
					int iDiffYcn = 0; //#i차이년수

					if (sStDt.compareTo(sEdDt) > 0) {
						sStDt = sOpDt;
						sEdDt = sDfrmPdEdDt;
					}

					iDiffMnCnt = KDateUtil.getYMDInterval(sStDt, sEdDt, 1)[1];
					iDiffYcn = KDateUtil.getYMDInterval(sStDt, sEdDt, 1)[0];
					LLog.debug.println("■■■■■ 104_s시작일자 ~ s종료일자 / 차이개월수 : "
							+ sStDt + "~" + sEdDt + "/ " + iDiffYcn + "년 / "
							+ iDiffMnCnt + "개월");
					if (iDiffMnCnt < 1) {

						// =============================================================================
						// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅
						// =============================================================================
						// [i월월말일자확인변경]  

						iMonEmthDtCfCh.setString("in_dt", sOpDt);
						iMonEmthDtCfCh.setLong("mn_cnt", 1);
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iMonEmthDtCfCh);
						rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

						// =============================================================================
						// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑
						// =============================================================================
						// [r월월말일자확인변경]

						sOpDt = rMonEmthDtCfCh.getString("otpt_dt");

						//유예기간 1개월이 입력될 경우 무조건 [s작업일자] == [s유예기간종료일자] 이므로 차이년수로 조건을 추가
						if (KLDataConvertUtil.equals(
								iAfdfInslChCondCu.getLong("pst_pd"), 1)) {

							if (KLDataConvertUtil.equals(sOpDt, sPstPdEdDt)
									&& iDiffYcn < 1) {
								sPrsYn = "N";
								rAfdfInslChCondCu.setLong("dfrm_mcn", lDfrmMcn);
								rAfdfInslChCondCu.setLong("insl_ttim",
										lLnPdTtim - lDfrmMcn);
								rAfdfInslChCondCu.setLong("insl_pa_tim", 0);
								rAfdfInslChCondCu.setString("nt_rpy_day",
										sPstPdEdDt);
							}
						} else {
							if (KLDataConvertUtil.equals(sOpDt, sPstPdEdDt)) {
								sPrsYn = "N";
								rAfdfInslChCondCu.setLong("dfrm_mcn", lDfrmMcn);
								rAfdfInslChCondCu.setLong("insl_ttim",
										lLnPdTtim - lDfrmMcn);
								rAfdfInslChCondCu.setLong("insl_pa_tim", 0);
								rAfdfInslChCondCu.setString("nt_rpy_day",
										sPstPdEdDt);
							}
						}

						LLog.debug.println("■■■■■ 105_작업일자  : " + sOpDt);
					}
				}
			}
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAfdfInslChCondCu);
		return rAfdfInslChCondCu;
	}

	/**
	 * 수납일련번호채번
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055102
	 * @logicalName   수납일련번호채번
	 * @param LData iRcvSeqGno i수납일련번호채번
	 * @return LData rRcvSeqGno r수납일련번호채번
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_수납일련번호채번
	 * 
	 */
	public LData getRcvSeq(LData iRcvSeqGno) throws LException {
		//#Return 변수 선언 및 초기화
		LData rRcvSeqGno = new LData(); //# r수납일련번호채번
										//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iLonRcvHisSeqInq = new LData(); //# i여신수납내역일련번호조회
		LData rLonRcvHisSeqInq = new LData(); //# r여신수납내역일련번호조회
		//#호출 컴포넌트 초기화
		LonRcvHisEbc lonRcvHisEbc = new LonRcvHisEbc(); //# 여신수납내역Ebi

		// =============================================================================
		// ######### CodeValidationBlock ##입력값 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			//대출번호
			if (KStringUtil.isEmpty(iRcvSeqGno.getString("ln_no"))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "대출번호" });
				existInvalidElement = true;
			}

			//대출일련번호
			if (KStringUtil.isEmpty(iRcvSeqGno.getLong("ln_seq"))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "대출일련번호" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##여신수납내역일련번호조회  입력값 세팅
		// =============================================================================
		iLonRcvHisSeqInq.setString("ln_no", iRcvSeqGno.getString("ln_no"));
		iLonRcvHisSeqInq.setLong("ln_seq", iRcvSeqGno.getLong("ln_seq"));

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_수납일련번호채번 - 여신수납내역일련번호조회 START");
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(iLonRcvHisSeqInq);
		rLonRcvHisSeqInq = lonRcvHisEbc.retvLonRcvHisSeq(iLonRcvHisSeqInq); //##여신수납내역일련번호조회

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 조립
		// =============================================================================
		rRcvSeqGno.setLong("rcv_seq", rLonRcvHisSeqInq.getLong("rcv_seq") + 1);

		if (LLog.debug.isEnabled()) {
			LLog.debug.println(rRcvSeqGno);
			LLog.debug.println("■■■ ACSD_수납일련번호채번 - 여신수납내역일련번호조회 END");
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rRcvSeqGno);
		return rRcvSeqGno;
	}

	/**
	 * 수납취소가능여부조회
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055113
	 * @logicalName   수납취소가능여부조회
	 * @param LData iRcvCnlPsYnInq i수납취소가능여부조회
	 * @return LData rRcvCnlPsYnInq r수납취소가능여부조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_수납취소가능여부조회
	 * 
	 */
	public LData retvRcvCnlPsYn(LData iRcvCnlPsYnInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rRcvCnlPsYnInq = new LData(); //# r수납취소가능여부조회
											//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData icAuthPossYnInq = new LData(); //# ic권한소유여부조회
		LData rcAuthPossYnInq = new LData(); //# rc권한소유여부조회
		LData iLonCnrBasLnNoListInq = new LData(); //# i여신계약기본대출번호목록조회
		LMultiData rLonCnrBasLnNoListInq = new LMultiData(); //# r여신계약기본대출번호목록조회
		LData iLonAutTrsBasInq = new LData(); //# i여신자동이체기본조회
		LData rLonAutTrsBasInq = new LData(); //# r여신자동이체기본조회
		LData iLonDfmAgtBdePrcHisLnNoListInq = new LData(); //# i여신출금동의일괄처리내역대출번호목록조회
		LMultiData rLonDfmAgtBdePrcHisLnNoListInq = new LMultiData(); //# r여신출금동의일괄처리내역대출번호목록조회
		LData iLonRcvHisLnNoListInq = new LData(); //# i여신수납내역대출번호목록조회
		LMultiData rLonRcvHisLnNoListInq = new LMultiData(); //# r여신수납내역대출번호목록조회
		LData iLonRcvBrafDtlBasListInq = new LData(); //# i여신수납전후상세기본목록조회
		LMultiData rLonRcvBrafDtlBasListInq = new LMultiData(); //# r여신수납전후상세기본목록조회
		LData iLonCnrBasInq = new LData(); //# i여신계약기본조회
		LData rLonCnrBasInq = new LData(); //# r여신계약기본조회
		LData iLonRcvCnlHisLnNoListInq = new LData(); //# i여신수납취소내역대출번호목록조회
		LMultiData rLonRcvCnlHisLnNoListInq = new LMultiData(); //# r여신수납취소내역대출번호목록조회
		LData iExcrTrmReqNcnInq = new LData(); //# i근저당해지신청건수조회
		LData rExcrTrmReqNcnInq = new LData(); //# r근저당해지신청건수조회
		LData iSpcBdRcnlHisLnNoMaxNoOdrInq = new LData(); //# i특수채권회수내역대출번호최대순번조회
		LData rSpcBdRcnlHisLnNoMaxNoOdrInq = new LData(); //# r특수채권회수내역대출번호최대순번조회
		LData iSpcBdRcnlHisInq = new LData(); //# i특수채권회수내역조회
		LData rSpcBdRcnlHisInq = new LData(); //# r특수채권회수내역조회
		LData iSpcBdBasInq = new LData(); //# i특수채권기본조회
		LData rSpcBdBasInq = new LData(); //# r특수채권기본조회
		LData iSpcBdRcnlBrafDtlInq = new LData(); //# i특수채권회수전후상세조회
		LData rSpcBdRcnlBrafDtlInq = new LData(); //# r특수채권회수전후상세조회
		//#호출 컴포넌트 초기화
		AuthMgtCpbc authMgtCpbc = new AuthMgtCpbc(); //# 권한관리Cpbi
		LonCnrBasEbc lonCnrBasEbc = new LonCnrBasEbc(); //# 여신계약기본Ebi
		LonAutTrsBasEbc lonAutTrsBasEbc = new LonAutTrsBasEbc(); //# 여신자동이체기본Ebi
		LonDfmAgtBdePrcHisEbc lonDfmAgtBdePrcHisEbc = new LonDfmAgtBdePrcHisEbc(); //# 여신출금동의일괄처리내역Ebi
		LonRcvHisEbc lonRcvHisEbc = new LonRcvHisEbc(); //# 여신수납내역Ebi
		LonRcvBrafDtlEbc lonRcvBrafDtlEbc = new LonRcvBrafDtlEbc(); //# 여신수납전후상세Ebi
		LonRcvCnlHisEbc lonRcvCnlHisEbc = new LonRcvCnlHisEbc(); //# 여신수납취소내역Ebi
		LonLnDlnHisEbc lonLnDlnHisEbc = new LonLnDlnHisEbc(); //# 여신대출거래내역Ebi
		SpcBdRcnlHisEbc spcBdRcnlHisEbc = new SpcBdRcnlHisEbc(); //# 특수채권회수내역Ebi
		SpcBdBasEbc spcBdBasEbc = new SpcBdBasEbc(); //# 특수채권기본Ebi
		SpcBdRcnlBrafDtlEbc spcBdRcnlBrafDtlEbc = new SpcBdRcnlBrafDtlEbc(); //# 특수채권회수전후상세Ebi

		// =============================================================================
		// ######### CodeValidationBlock ##입력값 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_수납취소가능여부조회 - 입력값 검증");
				LLog.debug.println(iRcvCnlPsYnInq);
			}

			if (KStringUtil.trimNisEmpty(iRcvCnlPsYnInq.getString("ln_no"))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "대출번호" });
				existInvalidElement = true;
			}

			if (KStringUtil.trimNisEmpty(iRcvCnlPsYnInq.getString("bd_dv_cd"))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "채권구분코드" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 세팅
		// =============================================================================
		String sNowDt = KDateUtil.getCurrentDate("yyyyMMdd"); //#s현재일자
		String sNowTi = KDateUtil.getCurrentTime("HHmmss"); //#s현재시간
		String sNowDtm = sNowDt + sNowTi; //#s현재일시
		String sFlpyDv = "0"; //#s완제구분
		String sLdgChgYn = "N"; //#s원장변동여부
		String sLonCslId = ""; //#s여신상담ID
		String sMstrYn = "N"; //#s마스터여부

		String sOcPatAuth = CommYnEynConst.IJT.getCode(); //#sOP파트권한

		//20210331 전액감면인경우 소급취소가능처리
		String sRtvPsYn = "N"; //#s소급가능여부

		//OP파트 권한확인
		{

			// =============================================================================
			// ######### GeneralCodeBlock ##권한소유여부조회  입력값 세팅
			// =============================================================================
			// [ic권한소유여부조회]  

			icAuthPossYnInq = new LData();
			icAuthPossYnInq.setString("om_no",
					iRcvCnlPsYnInq.getString("ope_id"));
			icAuthPossYnInq.setString("auth_id_val",
					LonAuthMgtCdConst.OC_PAT.getCode()); //OP파트

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_수납취소가능여부조회 - 권한소유여부조회  입력값 세팅");
				LLog.debug.println(icAuthPossYnInq);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(icAuthPossYnInq);
			rcAuthPossYnInq = authMgtCpbc.retvAuthPossYn(icAuthPossYnInq); //##권한소유여부조회

			// =============================================================================
			// ######### GeneralCodeBlock ##권한소유여부조회  결과값 맵핑
			// =============================================================================
			sOcPatAuth = rcAuthPossYnInq.getString("auth_poss_yn");
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##여신계약기본[대출번호]목록조회  입력값 세팅
		// =============================================================================
		iLonCnrBasLnNoListInq = new LData();
		rLonCnrBasLnNoListInq = new LMultiData();

		iLonCnrBasLnNoListInq.setString("ln_no",
				iRcvCnlPsYnInq.getString("ln_no"));

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_수납취소가능여부조회 - 여신계약기본lnNo목록조회  입력값 세팅");
			LLog.debug.println(iLonCnrBasLnNoListInq);
		}
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(iLonCnrBasLnNoListInq);
		rLonCnrBasLnNoListInq = lonCnrBasEbc
				.retvLstLonCnrBasByLnNo(iLonCnrBasLnNoListInq); //##여신계약기본[대출번호]목록조회
		if (rLonCnrBasLnNoListInq.getDataCount() <= 0) {

			// =============================================================================
			// ######### ExceptionCodeBlock ##여신계약기본 조회결과가 없습니다.
			// =============================================================================
			{
				throw new BizException("MZZZ00161", new String[] { "여신계약기본" });
			}
		}
		if (KLDataConvertUtil.equals(iRcvCnlPsYnInq.getString("bd_dv_cd"),
				BdDvCdConst.GEN_BD.getCode())) {
			if (KLDataConvertUtil.equals(
					rLonCnrBasLnNoListInq.getString("lon_pa_mth_cd", 0), "20") // 자동이체
					&& KLDataConvertUtil.equals(
							rLonCnrBasLnNoListInq.getString("ln_ss_cd", 0),
							"08") //완제
			) {
				try {

					// =============================================================================
					// ######### GeneralCodeBlock ##여신자동이체기본조회  입력값 세팅
					// =============================================================================
					iLonAutTrsBasInq.setString("ln_no",
							iRcvCnlPsYnInq.getString("ln_no"));

					if (LLog.debug.isEnabled()) {
						LLog.debug
								.println("■■■ ACSD_수납취소가능여부조회 - 여신자동이체기본조회  입력값 세팅");
						LLog.debug.println(iLonAutTrsBasInq);
					}
					LProtocolInitializeUtil
							.primitiveLMultiInitialize(iLonAutTrsBasInq);
					rLonAutTrsBasInq = lonAutTrsBasEbc
							.retvLonAutTrsBas(iLonAutTrsBasInq); //##여신자동이체기본조회
				} catch (LBizNotFoundException e) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##자동이체 조회결과가 없습니다.
					// =============================================================================
					{

						throw new BizException("MZZZ00161",
								new String[] { "자동이체" }, e);
					}
				}

				// =============================================================================
				// ######### GeneralCodeBlock ##여신출금동의일괄처리내역[대출번호]목록조회  입력값 세팅
				// =============================================================================
				// [i여신출금동의일괄처리내역대출번호목록조회]

				iLonDfmAgtBdePrcHisLnNoListInq.setString("ln_no",
						iRcvCnlPsYnInq.getString("ln_no"));
				iLonDfmAgtBdePrcHisLnNoListInq.setString("cs_fin_aco_id",
						rLonAutTrsBasInq.getString("fin_aco_id"));
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonDfmAgtBdePrcHisLnNoListInq);
				rLonDfmAgtBdePrcHisLnNoListInq = lonDfmAgtBdePrcHisEbc
						.retvLstLonDfmAgtBdePrcHisByLnNo(iLonDfmAgtBdePrcHisLnNoListInq); //##여신출금동의일괄처리내역[대출번호]목록조회
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##여신수납내역[대출번호]목록조회  입력값 세팅
			// =============================================================================
			iLonRcvHisLnNoListInq.setString("ln_no",
					iRcvCnlPsYnInq.getString("ln_no"));
			iLonRcvHisLnNoListInq.setLong("rcv_seq",
					iRcvCnlPsYnInq.getLong("rcv_seq"));
			iLonRcvHisLnNoListInq.setString("cnl_yn", "N");

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_수납취소가능여부조회 - 여신수납내역lnNo목록조회  입력값 세팅");
				LLog.debug.println(iLonRcvHisLnNoListInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iLonRcvHisLnNoListInq);
			rLonRcvHisLnNoListInq = lonRcvHisEbc
					.retvLstLonRcvHisByLnNo(iLonRcvHisLnNoListInq); //##여신수납내역[대출번호]목록조회
			if (rLonRcvHisLnNoListInq.getDataCount() <= 0) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 미등록 수납건입니다.
				// =============================================================================
				{
					throw new BizException("MLRL00305", new String[] { "수납건" });
				}
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##조회결과 Dto 생성
			// =============================================================================
			LData tLonRcvHis = new LData(); //#t여신수납내역
			tLonRcvHis = rLonRcvHisLnNoListInq.getLData(0);
			if (KLDataConvertUtil.equals(
					tLonRcvHis.getString("rcv_prc_mth_cd"), "31") //선기장이므로 처리일자로 체크
			) {
				if (KLDataConvertUtil.notEquals(KStringUtil.substring(
						tLonRcvHis.getString("fs_in_dtm"), 0, 8), sNowDt)) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 당일 수납건이 아니므로 취소할 수 없습니다.
					// =============================================================================
					{
						throw new BizException("MLRL00306");
					}
				}
			} else {

				// =============================================================================
				// ######### GeneralCodeBlock ##소급가능여부체크
				// =============================================================================
				if (KLDataConvertUtil.equals(
						tLonRcvHis.getString("rcv_prc_mth_cd"), "41")
						&& KLDataConvertUtil.equals(
								KStringUtil.substring(
										tLonRcvHis.getString("rcv_dt"), 0, 6),
								KStringUtil.substring(sNowDt, 0, 6))) {
					sRtvPsYn = "Y";
				}
				if (//20210323 회계미발생건은 소급취소가능토록 변경(SRM2102-04709)
				KLDataConvertUtil.notEquals(tLonRcvHis.getString("rcv_dt"),
						sNowDt) && KLDataConvertUtil.notEquals(sRtvPsYn, "Y")) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 당일 수납건이 아니므로 취소할 수 없습니다.
					// =============================================================================
					{
						throw new BizException("MLRL00306");
					}
				}
			}
			if ((tLonRcvHis.getBigDecimal("rei_tam")).compareTo(new BigDecimal(
					"1000000")) >= 0 //영수금액-일백만원이상 취소시 MASTER 승인권한
			) {

				// =============================================================================
				// ######### GeneralCodeBlock ##완제구분 '2' 변경
				// =============================================================================
				sFlpyDv = "2";
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##여신수납전후상세[기본]목록조회  입력값 세팅
			// =============================================================================
			iLonRcvBrafDtlBasListInq.setString("ln_no",
					iRcvCnlPsYnInq.getString("ln_no"));
			iLonRcvBrafDtlBasListInq.setLong("rcv_seq",
					iRcvCnlPsYnInq.getLong("rcv_seq"));
			iLonRcvBrafDtlBasListInq.setString("braf_dv_cd",
					BrafDvCdConst.AF.getCode()); //후

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_수납취소가능여부조회 - 여신수납전후상세bas목록조회  입력값 세팅");
				LLog.debug.println(iLonRcvBrafDtlBasListInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iLonRcvBrafDtlBasListInq);
			rLonRcvBrafDtlBasListInq = lonRcvBrafDtlEbc
					.retvLstLonRcvBrafDtlByBas(iLonRcvBrafDtlBasListInq); //##여신수납전후상세[기본]목록조회
			for (int inx = 0, inxLoopSize = rLonRcvBrafDtlBasListInq
					.getDataCount(); inx < inxLoopSize; inx++) {
				LData tLonAfrvDtl = rLonRcvBrafDtlBasListInq.getLData(inx);
				LProtocolInitializeUtil.primitiveLMultiInitialize(tLonAfrvDtl);

				// =============================================================================
				// ######### GeneralCodeBlock ##여신계약기본조회  입력값 세팅
				// =============================================================================
				iLonCnrBasInq = new LData();
				rLonCnrBasInq = new LData();

				iLonCnrBasInq
						.setString("ln_no", tLonAfrvDtl.getString("ln_no"));
				iLonCnrBasInq.setLong("ln_seq", tLonAfrvDtl.getLong("ln_seq"));
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonCnrBasInq);
				rLonCnrBasInq = lonCnrBasEbc.retvLonCnrBas(iLonCnrBasInq); //##여신계약기본조회
				if (KLDataConvertUtil.notEquals(
						tLonAfrvDtl.getBigDecimal("ln_bal"),
						rLonCnrBasInq.getBigDecimal("ln_bal"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("cmpe_ls_dt"),
								rLonCnrBasInq.getString("cmpe_ls_dt"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("pa_fsr_dt"),
								rLonCnrBasInq.getString("pa_fsr_dt"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getLong("insl_pa_tim"),
								rLonCnrBasInq.getLong("insl_pa_tim"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getLong("ppmt_ap_dcn"),
								rLonCnrBasInq.getLong("ppmt_ap_dcn"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getLong("ppmt_rmd_dcn"),
								rLonCnrBasInq.getLong("ppmt_rmd_dcn"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("nt_rpy_dt"),
								rLonCnrBasInq.getString("nt_rpy_dt"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getBigDecimal("nt_rpy_amt"),
								rLonCnrBasInq.getBigDecimal("nt_rpy_amt"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("ls_rei_dt"),
								rLonCnrBasInq.getString("ls_rei_dt"))
						|| KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("ln_ss_cd"),
								rLonCnrBasInq.getString("ln_ss_cd"))) {

					// =============================================================================
					// ######### GeneralCodeBlock ##원장변동여부 'Y' 변경
					// =============================================================================
					sLdgChgYn = "Y";

					if (LLog.debug.isEnabled()) {
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 대출잔액     : "
								+ tLonAfrvDtl.getBigDecimal("ln_bal") + "/"
								+ rLonCnrBasInq.getBigDecimal("ln_bal"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 이수최종일자 : "
								+ tLonAfrvDtl.getString("cmpe_ls_dt") + "/"
								+ rLonCnrBasInq.getString("cmpe_ls_dt"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 납입응당일자 : "
								+ tLonAfrvDtl.getString("pa_fsr_dt") + "/"
								+ rLonCnrBasInq.getString("pa_fsr_dt"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 할부납입회차 : "
								+ tLonAfrvDtl.getLong("insl_pa_tim") + "/"
								+ rLonCnrBasInq.getLong("insl_pa_tim"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 선납적용일수 : "
								+ tLonAfrvDtl.getLong("ppmt_ap_dcn") + "/"
								+ rLonCnrBasInq.getLong("ppmt_ap_dcn"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 선납잔여일수 : "
								+ tLonAfrvDtl.getLong("ppmt_rmd_dcn") + "/"
								+ rLonCnrBasInq.getLong("ppmt_rmd_dcn"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 차기상환일자 : "
								+ tLonAfrvDtl.getString("nt_rpy_dt") + "/"
								+ rLonCnrBasInq.getString("nt_rpy_dt"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 차기상환금액 : "
								+ tLonAfrvDtl.getBigDecimal("nt_rpy_amt") + "/"
								+ rLonCnrBasInq.getBigDecimal("nt_rpy_amt"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 최종영수일자 : "
								+ tLonAfrvDtl.getString("ls_rei_dt") + "/"
								+ rLonCnrBasInq.getString("ls_rei_dt"));
						LLog.debug.println("■■■ 원장비교 수납후/계약기본 대출상태코드 : "
								+ tLonAfrvDtl.getString("ln_ss_cd") + "/"
								+ rLonCnrBasInq.getString("ln_ss_cd"));

					}

					if (LLog.err.isEnabled()) {
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getBigDecimal("ln_bal"),
								rLonCnrBasInq.getBigDecimal("ln_bal"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 대출잔액     불일치 : "
									+ tLonAfrvDtl.getBigDecimal("ln_bal")
									+ "/"
									+ rLonCnrBasInq.getBigDecimal("ln_bal"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("cmpe_ls_dt"),
								rLonCnrBasInq.getString("cmpe_ls_dt"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 이수최종일자 불일치 : "
									+ tLonAfrvDtl.getString("cmpe_ls_dt") + "/"
									+ rLonCnrBasInq.getString("cmpe_ls_dt"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("pa_fsr_dt"),
								rLonCnrBasInq.getString("pa_fsr_dt"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 납입응당일자 불일치 : "
									+ tLonAfrvDtl.getString("pa_fsr_dt") + "/"
									+ rLonCnrBasInq.getString("pa_fsr_dt"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getLong("insl_pa_tim"),
								rLonCnrBasInq.getLong("insl_pa_tim"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 할부납입회차 불일치 : "
									+ tLonAfrvDtl.getLong("insl_pa_tim") + "/"
									+ rLonCnrBasInq.getLong("insl_pa_tim"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getLong("ppmt_ap_dcn"),
								rLonCnrBasInq.getLong("ppmt_ap_dcn"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 선납적용일수 불일치 : "
									+ tLonAfrvDtl.getLong("ppmt_ap_dcn") + "/"
									+ rLonCnrBasInq.getLong("ppmt_ap_dcn"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getLong("ppmt_rmd_dcn"),
								rLonCnrBasInq.getLong("ppmt_rmd_dcn"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 선납잔여일수 불일치 : "
									+ tLonAfrvDtl.getLong("ppmt_rmd_dcn") + "/"
									+ rLonCnrBasInq.getLong("ppmt_rmd_dcn"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("nt_rpy_dt"),
								rLonCnrBasInq.getString("nt_rpy_dt"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 차기상환일자 불일치 : "
									+ tLonAfrvDtl.getString("nt_rpy_dt") + "/"
									+ rLonCnrBasInq.getString("nt_rpy_dt"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getBigDecimal("nt_rpy_amt"),
								rLonCnrBasInq.getBigDecimal("nt_rpy_amt"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 차기상환금액 불일치 : "
									+ tLonAfrvDtl.getBigDecimal("nt_rpy_amt")
									+ "/"
									+ rLonCnrBasInq.getBigDecimal("nt_rpy_amt"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("ls_rei_dt"),
								rLonCnrBasInq.getString("ls_rei_dt"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 최종영수일자 불일치 : "
									+ tLonAfrvDtl.getString("ls_rei_dt") + "/"
									+ rLonCnrBasInq.getString("ls_rei_dt"));
						}
						if (KLDataConvertUtil.notEquals(
								tLonAfrvDtl.getString("ln_ss_cd"),
								rLonCnrBasInq.getString("ln_ss_cd"))) {
							LLog.err.println("■■■ 원장비교 수납후/계약기본 대출상태코드 불일치 : "
									+ tLonAfrvDtl.getString("ln_ss_cd") + "/"
									+ rLonCnrBasInq.getString("ln_ss_cd"));
						}
					}
				}
				if ((tLonAfrvDtl.getBigDecimal("ln_bal"))
						.compareTo(new BigDecimal("0")) <= 0) {

					// =============================================================================
					// ######### GeneralCodeBlock ##완제구분 1 변경, 여신상담ID 입력
					// =============================================================================
					sFlpyDv = "1";
					sLonCslId = rLonCnrBasInq.getString("lon_csl_id");
				}
			}
			if (KLDataConvertUtil.equals(sLdgChgYn, "Y")) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 수납 후 원장의 내역이 변동되었습니다. 취소처리가 불가합니다.
				// =============================================================================
				{
					throw new BizException("MLRL00292");
				}
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##여신수납취소내역[대출번호]목록조회  입력값 세팅
			// =============================================================================
			iLonRcvCnlHisLnNoListInq.setString("ln_no",
					iRcvCnlPsYnInq.getString("ln_no"));
			iLonRcvCnlHisLnNoListInq.setLong("rcv_seq",
					iRcvCnlPsYnInq.getLong("rcv_seq"));
			iLonRcvCnlHisLnNoListInq.setString("spc_bd_reg_yn", "N");

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_수납취소가능여부조회 - 여신수납취소내역lnNo목록조회  입력값 세팅");
				LLog.debug.println(iLonRcvHisLnNoListInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iLonRcvCnlHisLnNoListInq);
			rLonRcvCnlHisLnNoListInq = lonRcvCnlHisEbc
					.retvLstLonRcvCnlHisByLnNo(iLonRcvCnlHisLnNoListInq); //##여신수납취소내역[대출번호]목록조회
			if (rLonRcvCnlHisLnNoListInq.getDataCount() > 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신수납취소내역[대출번호]목록조회  결과값 맵핑
				// =============================================================================
				LData tLonRcvCnlHis = new LData(); //#t여신수납취소내역
				tLonRcvCnlHis = rLonRcvCnlHisLnNoListInq.getLData(0);
				if (KLDataConvertUtil.equals(
						tLonRcvCnlHis.getString("cnl_cns_dv_cd"), "1") //취소요청
				) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 이미취소요청된건입니다.
					// =============================================================================
					{
						throw new BizException("MLRL00307",
								new String[] { "취소요청" });
					}
				} else if (KLDataConvertUtil.equals(
						tLonRcvCnlHis.getString("cnl_cns_dv_cd"), "3") //취소완료
				) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 이미취소완료된건입니다.
					// =============================================================================
					{
						throw new BizException("MLRL00307",
								new String[] { "취소완료" });
					}
				}
			} else {
				if (KLDataConvertUtil.notEquals(sOcPatAuth,
						CommYnEynConst.AFFRM.getCode())
						&& KLDataConvertUtil.notEquals(
								iRcvCnlPsYnInq.getString("prc_orz_cd"),
								tLonRcvHis.getString("rcv_orz_cd"))) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 처리 권한이 존재하지 않습니다.
					// =============================================================================
					{
						throw new BizException("MZZZ00151");
					}
				}
			}
			if (KLDataConvertUtil.equals(sFlpyDv, "1") //전액상환시 근저당 말소건은 취소불가
			) {

				// =============================================================================
				// ######### GeneralCodeBlock ##근저당해지신청건수조회  입력값 세팅
				// =============================================================================
				// [i근저당해지신청건수조회]  
				iExcrTrmReqNcnInq.setString("ln_no",
						iRcvCnlPsYnInq.getString("ln_no"));
				iExcrTrmReqNcnInq.setString("scy_trm_req_dt", sNowDt);
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iExcrTrmReqNcnInq);
				rExcrTrmReqNcnInq = lonLnDlnHisEbc
						.retvExcrTrmReqNcn(iExcrTrmReqNcnInq); //##근저당해지신청건수조회
				if (rExcrTrmReqNcnInq.getLong("ncn") > 0) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 담보해지완료건으로 수납취소가 불가합니다.
					// =============================================================================
					{
						throw new BizException("MLRL00308",
								new String[] { "담보해지완료" });
					}
				}
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##여신수납전후상세[기본]목록조회  입력값 세팅
			// =============================================================================
			iLonRcvBrafDtlBasListInq = new LData();
			rLonRcvBrafDtlBasListInq = new LMultiData();

			iLonRcvBrafDtlBasListInq.setString("ln_no",
					iRcvCnlPsYnInq.getString("ln_no"));
			iLonRcvBrafDtlBasListInq.setLong("rcv_seq",
					iRcvCnlPsYnInq.getLong("rcv_seq"));
			iLonRcvBrafDtlBasListInq.setString("braf_dv_cd",
					BrafDvCdConst.BEF.getCode());

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_수납취소가능여부조회 - 여신수납전후상세bas목록조회  입력값 세팅");
				LLog.debug.println(iLonRcvBrafDtlBasListInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iLonRcvBrafDtlBasListInq);
			rLonRcvBrafDtlBasListInq = lonRcvBrafDtlEbc
					.retvLstLonRcvBrafDtlByBas(iLonRcvBrafDtlBasListInq); //##여신수납전후상세[기본]목록조회

			// =============================================================================
			// ######### GeneralCodeBlock ##여신수납전후상세[기본]목록조회  결과값 맵핑
			// =============================================================================
			LData tLonBfrvDtl = new LData(); //#t여신수납전상세
			tLonBfrvDtl = rLonRcvBrafDtlBasListInq.getLData(0);
			if (KLDataConvertUtil.equals(KStringUtil.substring(
					tLonBfrvDtl.getString("fs_in_dtm"), 0, 8), sNowDt)) {
				if (KDateUtil.addTime(tLonBfrvDtl.getString("fs_in_dtm"), 1, 0,
						0).compareTo(sNowDtm) < 0) {

					// =============================================================================
					// ######### GeneralCodeBlock ##마스터전송여부 Y 세팅
					// =============================================================================
					sMstrYn = "Y";
				}
			}
		} else if (KLDataConvertUtil.equals(
				iRcvCnlPsYnInq.getString("bd_dv_cd"),
				BdDvCdConst.SPC_BD.getCode())) {

			// =============================================================================
			// ######### GeneralCodeBlock ##특수채권회수내역[대출번호]최대순번조회  입력값 세팅
			// =============================================================================
			iSpcBdRcnlHisLnNoMaxNoOdrInq.setString("ln_no",
					iRcvCnlPsYnInq.getString("ln_no"));
			iSpcBdRcnlHisLnNoMaxNoOdrInq.setString("cnl_yn", "N");

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_수납취소가능여부조회 - 특수채권회수내역대출번호최대순번조회  입력값 세팅");
				LLog.debug.println(iSpcBdRcnlHisLnNoMaxNoOdrInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iSpcBdRcnlHisLnNoMaxNoOdrInq);
			rSpcBdRcnlHisLnNoMaxNoOdrInq = spcBdRcnlHisEbc
					.retvSpcBdRcnlHisByLnNo(iSpcBdRcnlHisLnNoMaxNoOdrInq); //##특수채권회수내역[대출번호]최대순번조회
			if (rSpcBdRcnlHisLnNoMaxNoOdrInq.getLong("spc_bd_rcnl_seq") > iRcvCnlPsYnInq
					.getLong("rcv_seq")) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 이후 회수건이 존재하므로 취소할 수 없습니다.
				// =============================================================================
				{
					throw new BizException("MLRL00309");
				}
			}
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##특수채권회수내역조회  입력값 세팅
				// =============================================================================
				iSpcBdRcnlHisInq.setString("ln_no",
						iRcvCnlPsYnInq.getString("ln_no"));
				iSpcBdRcnlHisInq.setLong("spc_bd_rcnl_seq",
						iRcvCnlPsYnInq.getLong("rcv_seq"));

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_수납취소가능여부조회 - 특수채권회수내역조회  입력값 세팅");
					LLog.debug.println(iSpcBdRcnlHisInq);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iSpcBdRcnlHisInq);
				rSpcBdRcnlHisInq = spcBdRcnlHisEbc
						.retvSpcBdRcnlHis(iSpcBdRcnlHisInq); //##특수채권회수내역조회
			} catch (LBizNotFoundException e) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 미등록 회수내역입니다.
				// =============================================================================
				{

					throw new BizException("MLRL00305",
							new String[] { "회수내역" }, e);
				}
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##합계영수금액 계산
			// =============================================================================
			BigDecimal bdRcnlIncpPcp = rSpcBdRcnlHisInq
					.getBigDecimal("rcnl_incp_pcp"); //#bd회수편입원금
			BigDecimal bdAaIrt = rSpcBdRcnlHisInq
					.getBigDecimal("rcnl_incp_cntb_irt"); //#bd연체이자

			BigDecimal bdEtcIrt = ((rSpcBdRcnlHisInq
					.getBigDecimal("rcnl_incp_pttm_amt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("rcnl_afic_iadt_amt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("rcnl_afic_unc_irt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("rcnl_incp_cntb_irt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("rcnl_prpn_dlay_irt")))
					.subtract(
							(rSpcBdRcnlHisInq
									.getBigDecimal("raex_incp_pttm_amt"))
									.add(rSpcBdRcnlHisInq
											.getBigDecimal("raex_afic_iadt_amt"))
									.add(rSpcBdRcnlHisInq
											.getBigDecimal("raex_afic_unc_irt"))
									.add(rSpcBdRcnlHisInq
											.getBigDecimal("raex_incp_cntb_irt"))
									.add(rSpcBdRcnlHisInq
											.getBigDecimal("raex_prpn_dlay_irt")))
					.subtract(bdRcnlIncpPcp.add(bdAaIrt)); //#bd기타이자

			BigDecimal bdTotReiAmt = bdEtcIrt.add(bdRcnlIncpPcp).add(bdAaIrt); //#bd합계영수금액

			//20210331 전액감면체크
			BigDecimal bdRcvAmt = ((rSpcBdRcnlHisInq
					.getBigDecimal("rcnl_incp_pttm_amt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("rcnl_afic_iadt_amt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("rcnl_afic_unc_irt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("rcnl_incp_cntb_irt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("rcnl_prpn_dlay_irt"))); //#bd수납금액

			BigDecimal bdRaexAmt = ((rSpcBdRcnlHisInq
					.getBigDecimal("raex_incp_pttm_amt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("raex_afic_iadt_amt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("raex_afic_unc_irt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("raex_incp_cntb_irt"))
					.add(rSpcBdRcnlHisInq.getBigDecimal("raex_prpn_dlay_irt"))); //#bd감면금액

			if (KLDataConvertUtil.equals(bdRcvAmt, bdRaexAmt)
					&& KLDataConvertUtil.equals(KStringUtil.substring(
							rSpcBdRcnlHisInq.getString("rcnl_prc_dt"), 0, 6),
							KStringUtil.substring(sNowDt, 0, 6))) {
				sRtvPsYn = "Y";
			}
			if (//20210323 회계미발생건은 소급취소가능토록 변경(SRM2102-04709)
			KLDataConvertUtil.notEquals(
					rSpcBdRcnlHisInq.getString("rcnl_prc_dt"), sNowDt)
					&& KLDataConvertUtil.notEquals(sRtvPsYn, "Y")) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 당일 회수건이 아니므로 취소할 수 없습니다.
				// =============================================================================
				{
					throw new BizException("MLRL00310");
				}
			}
			if (KLDataConvertUtil.notEquals(
					iRcvCnlPsYnInq.getString("prc_orz_cd"),
					rSpcBdRcnlHisInq.getString("rcv_orz_cd"))
					&& KLDataConvertUtil.notEquals(sOcPatAuth,
							CommYnEynConst.AFFRM.getCode())
					&& KLDataConvertUtil.equals(KStringUtil
							.trimNisEmpty(rSpcBdRcnlHisInq
									.getString("jrnl_hub_id")), false)) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 처리권한이 존재하지 않습니다.
				// =============================================================================
				{
					throw new BizException("MZZZ00151");
				}
			}
			if (bdTotReiAmt.compareTo(new BigDecimal("1000000")) >= 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##완제구분 2 세팅
				// =============================================================================
				sFlpyDv = "2";
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##특수채권기본조회  입력값 세팅
			// =============================================================================
			iSpcBdBasInq.setString("ln_no", iRcvCnlPsYnInq.getString("ln_no"));

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_수납취소가능여부조회 - 특수채권기본조회  입력값 세팅");
				LLog.debug.println(iSpcBdBasInq);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(iSpcBdBasInq);
			rSpcBdBasInq = spcBdBasEbc.retvSpcBdBas(iSpcBdBasInq); //##특수채권기본조회

			// =============================================================================
			// ######### GeneralCodeBlock ##특수채권기본조회  결과값 맵핑 - 회수잔액
			// =============================================================================
			BigDecimal bdRcnlBal = rSpcBdBasInq.getBigDecimal("spc_bd_bal"); //#bd회수잔액
			if (bdRcnlBal.compareTo(new BigDecimal("0")) <= 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##완제구분 1 세팅
				// =============================================================================
				sFlpyDv = "1";
			}
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##특수채권회수전후상세조회  입력값 세팅
				// =============================================================================
				iSpcBdRcnlBrafDtlInq.setString("ln_no",
						iRcvCnlPsYnInq.getString("ln_no"));
				iSpcBdRcnlBrafDtlInq.setLong("spc_bd_rcnl_seq",
						iRcvCnlPsYnInq.getLong("rcv_seq"));
				iSpcBdRcnlBrafDtlInq.setString("braf_dv_cd",
						BrafDvCdConst.BEF.getCode());

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_수납취소가능여부조회 - 특수채권회수전후상세조회  입력값 세팅");
					LLog.debug.println(iSpcBdRcnlBrafDtlInq);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iSpcBdRcnlBrafDtlInq);
				rSpcBdRcnlBrafDtlInq = spcBdRcnlBrafDtlEbc
						.retvSpcBdRcnlBrafDtl(iSpcBdRcnlBrafDtlInq); //##특수채권회수전후상세조회
				if (KLDataConvertUtil.equals(KStringUtil.substring(
						rSpcBdRcnlBrafDtlInq.getString("fs_in_dtm"), 0, 8),
						sNowDt)) {
					if (KDateUtil.addTime(
							rSpcBdRcnlBrafDtlInq.getString("fs_in_dtm"), 1, 0,
							0).compareTo(sNowDtm) < 0) {

						// =============================================================================
						// ######### GeneralCodeBlock ##마스터전송여부 Y 세팅
						// =============================================================================
						sMstrYn = "Y";
					}
				}
			} catch (LBizNotFoundException e) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 회수전사항 조회결과가 없습니다.
				// =============================================================================
				{

					throw new BizException("MZZZ00161",
							new String[] { "회수전사항" }, e);
				}
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 입력
		// =============================================================================
		rRcvCnlPsYnInq.setString("mstr_yn", sMstrYn);

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_수납취소가능여부조회 - 결과값 입력");
			LLog.debug.println(rRcvCnlPsYnInq);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rRcvCnlPsYnInq);
		return rRcvCnlPsYnInq;
	}

	/**
	 * 여신계약기본원장수납검증
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055103
	 * @logicalName   여신계약기본원장수납검증
	 * @param LData iLonCnrBasLdgRcvVrf i여신계약기본원장수납검증
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_여신계약기본원장수납검증
	 * 
	 */
	public void verifyLonCnrBasLdgRcv(LData iLonCnrBasLdgRcvVrf)
			throws LException {
		//#Return 변수 선언 및 초기화
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iOrzBasInq = new LData(); //# i조직기본조회
		LData rOrzBasInq = new LData(); //# r조직기본조회
		LData iLonRcvImpiHisRcvImpiRsnCdListInq = new LData(); //# i여신수납불가내역수납불가사유코드목록조회
		LMultiData rLonRcvImpiHisRcvImpiRsnCdListInq = new LMultiData(); //# r여신수납불가내역수납불가사유코드목록조회
		LData icAuthPossYnInq = new LData(); //# ic권한소유여부조회
		LData rcAuthPossYnInq = new LData(); //# rc권한소유여부조회
		LData iLonWickRdmyClsgInq = new LData(); //# i여신창구시재마감조회
		LData rLonWickRdmyClsgInq = new LData(); //# r여신창구시재마감조회
		LData iLonBdeTrsHisLnNoNcnInq = new LData(); //# i여신일괄이체내역대출번호건수조회
		LData rLonBdeTrsHisLnNoNcnInq = new LData(); //# r여신일괄이체내역대출번호건수조회
		LData iLonImdtTrsHisLnNoNcnInq = new LData(); //# i여신즉시이체내역대출번호건수조회
		LData rLonImdtTrsHisLnNoNcnInq = new LData(); //# r여신즉시이체내역대출번호건수조회
		LData iLonRsvTrsHisLnNoListInq = new LData(); //# i여신예약이체내역대출번호목록조회
		LMultiData rLonRsvTrsHisLnNoListInq = new LMultiData(); //# r여신예약이체내역대출번호목록조회
		LData iExlnLnHisExlnLnNoListInq = new LData(); //# i대환대출내역대환대출번호목록조회
		LMultiData rExlnLnHisExlnLnNoListInq = new LMultiData(); //# r대환대출내역대환대출번호목록조회
		LData iLonCnrBasLonCslIdListInq = new LData(); //# i여신계약기본여신상담ID목록조회
		LMultiData rLonCnrBasLonCslIdListInq = new LMultiData(); //# r여신계약기본여신상담ID목록조회
		LData iLonImdtTrsHisTrsSsCd2ListInq = new LData(); //# i여신즉시이체내역이체상태코드2목록조회
		LMultiData rLonImdtTrsHisTrsSsCd2ListInq = new LMultiData(); //# r여신즉시이체내역이체상태코드2목록조회
		LData iCrpCondChRcvReqHisCrpCondChSsCdNcnInq = new LData(); //# iCRP조건변경수납신청내역CRP조건변경상태코드건수조회
		LData rCrpCondChRcvReqHisCrpCondChSsCdNcnInq = new LData(); //# rCRP조건변경수납신청내역CRP조건변경상태코드건수조회
		//#호출 컴포넌트 초기화
		OrzBasEbc orzBasEbc = new OrzBasEbc(); //# 조직기본Ebi
		LonRcvImpiHisEbc lonRcvImpiHisEbc = new LonRcvImpiHisEbc(); //# 여신수납불가내역Ebi
		AuthMgtCpbc authMgtCpbc = new AuthMgtCpbc(); //# 권한관리Cpbi
		LonWickRdmyClsgEbc lonWickRdmyClsgEbc = new LonWickRdmyClsgEbc(); //# 여신창구시재마감Ebi
		LonBdeTrsHisEbc lonBdeTrsHisEbc = new LonBdeTrsHisEbc(); //# 여신일괄이체내역Ebi
		LonImdtTrsHisEbc lonImdtTrsHisEbc = new LonImdtTrsHisEbc(); //# 여신즉시이체내역Ebi
		LonRsvTrsHisEbc lonRsvTrsHisEbc = new LonRsvTrsHisEbc(); //# 여신예약이체내역Ebi
		ExlnLnHisEbc exlnLnHisEbc = new ExlnLnHisEbc(); //# 대환대출내역Ebi
		LonCnrBasEbc lonCnrBasEbc = new LonCnrBasEbc(); //# 여신계약기본Ebi
		CrpCondChRcvReqHisEbc crpCondChRcvReqHisEbc = new CrpCondChRcvReqHisEbc(); //# CRP조건변경수납신청내역Ebi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 설정
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ 여신계약기본원장수납검증Cpbi START");
			LLog.debug.println(iLonCnrBasLdgRcvVrf);
		}

		String sNowDt = KDateUtil.getCurrentDate("yyyyMMdd"); //#s현재일자
		//String [s현재시간] = KDateUtil.getCurrentTime("HHmmss");

		String sAcnyOrzCd = (((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
				.getString("acny_orz_cd")); //#s경리조직코드
		String sPrcOrzCd = (((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
				.getString("prc_orz_cd")); //#s처리조직코드
		String sPrcOmNo = (((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
				.getString("prc_om_no")); //#s처리조직원번호

		if (KLDataConvertUtil.equals(KStringUtil.trimNisEmpty(sPrcOrzCd), true)) {
			sPrcOrzCd = KContextUtil.getBranchNumber();
		}

		if (KLDataConvertUtil.equals(KStringUtil.trimNisEmpty(sPrcOmNo), true)) {
			sPrcOmNo = KContextUtil.getOptorEnob();
		}

		// =============================================================================
		// ######### CodeValidationBlock ##입력 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			if (KStringUtil
					.isEmpty((((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
							.getString("ln_no")))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "대출번호" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}
		if (KLDataConvertUtil
				.equals(KStringUtil.trimNisEmpty(sAcnyOrzCd), true)) {
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##조직기본조회  입력값 세팅
				// =============================================================================
				iOrzBasInq = new LData();
				iOrzBasInq.setString("orz_cd", sPrcOrzCd);

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신계약기본원장수납검증 - 조직기본조회  입력값 세팅");
					LLog.debug.println(iOrzBasInq);
				}
				LProtocolInitializeUtil.primitiveLMultiInitialize(iOrzBasInq);
				rOrzBasInq = orzBasEbc.retvOrzBas(iOrzBasInq); //##조직기본조회

				// =============================================================================
				// ######### GeneralCodeBlock ##경리조직코드 입력
				// =============================================================================
				sAcnyOrzCd = rOrzBasInq.getString("acny_prc_orz_cd");
			} catch (LBizNotFoundException e) {
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("tot_rei_amt_chck_yn")), "Y")) {

			// =============================================================================
			// ######### CodeValidationBlock ##합계영수금액 검증
			// =============================================================================
			{
				LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
				boolean existInvalidElement = false;

				if ((((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
						.getBigDecimal("tot_rei_amt"))
						.compareTo(new BigDecimal("0")) < 0) {
					bizExceptionMessage.setBizExceptionMessage("MZZZ00083",
							new String[] { "영수금액" });
					existInvalidElement = true;
				}

				if (existInvalidElement) {
					bizExceptionMessage.throwBizException();
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("rcv_impi_chck_yn")), "Y")) {
			if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
					.get("basMtr")).getString("rcv_impi_yn")), "Y")) {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신수납불가내역[수납불가사유코드]목록조회  입력값 세팅
				// =============================================================================
				iLonRcvImpiHisRcvImpiRsnCdListInq.setString("ln_no",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getString("ln_no")));
				iLonRcvImpiHisRcvImpiRsnCdListInq.setString("rcv_impi_yn", "Y");
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonRcvImpiHisRcvImpiRsnCdListInq);
				rLonRcvImpiHisRcvImpiRsnCdListInq = lonRcvImpiHisEbc
						.retvLstLonRcvImpiHisByRcvImpiRsnCd(iLonRcvImpiHisRcvImpiRsnCdListInq); //##여신수납불가내역[수납불가사유코드]목록조회
				if (rLonRcvImpiHisRcvImpiRsnCdListInq.getDataCount() > 0) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 수납불가능 대출번호입니다.
					// =============================================================================
					{
						throw new BizException("MLRL00211",
								new String[] { "수납불가능" });
					}
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("ftr_dt_chck_yn")), "Y")) {

			// =============================================================================
			// ######### CodeValidationBlock ##영수일자가 미래날짜인 경우 수납불가
			// =============================================================================
			{
				LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
				boolean existInvalidElement = false;

				if (sNowDt.compareTo((((LData) iLonCnrBasLdgRcvVrf
						.get("basMtr")).getString("rei_dt"))) < 0) {
					bizExceptionMessage.setBizExceptionMessage("MLRL00212",
							new String[] { "" });
					existInvalidElement = true;
				}

				if (existInvalidElement) {
					bizExceptionMessage.throwBizException();
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("past_dt_chck_yn")), "Y")) {

			// =============================================================================
			// ######### GeneralCodeBlock ##권한소유여부조회  입력값 세팅
			// =============================================================================
			icAuthPossYnInq = new LData();
			icAuthPossYnInq.setString("om_no", (((LData) iLonCnrBasLdgRcvVrf
					.get("basMtr")).getString("prc_om_no")));
			icAuthPossYnInq.setString("auth_id_val",
					LonAuthMgtCdConst.OC_PAT.getCode()); //OP파트(여러개 찾아올시 |을 넣어서 구분해주면됨)

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_여신계약기본원장수납검증 - 권한소유여부조회  입력값 세팅");
				LLog.debug.println(icAuthPossYnInq);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(icAuthPossYnInq);
			rcAuthPossYnInq = authMgtCpbc.retvAuthPossYn(icAuthPossYnInq); //##권한소유여부조회
			if (KLDataConvertUtil.notEquals(
					rcAuthPossYnInq.getString("auth_poss_yn"),
					CommYnEynConst.AFFRM.getCode())) {

				// =============================================================================
				// ######### CodeValidationBlock ##여신총괄지원팀외에는 과거날짜 수납불가 오류처리 : 여신총괄지원팀외에는 과거날짜로 수납불가합니다.
				// =============================================================================
				{
					LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
					boolean existInvalidElement = false;

					if (LLog.debug.isEnabled()) {
						LLog.debug.println("■■■ ACSD_여신계약기본원장수납검증 과거날짜 확인");
						LLog.debug.println((((LData) iLonCnrBasLdgRcvVrf
								.get("basMtr")).getString("rei_dt")));
						LLog.debug.println(sNowDt);
					}

					// OP파트 권한자외에는 과거날짜로 수납불가
					if ((((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
							.getString("rei_dt")).compareTo(sNowDt) < 0) {
						bizExceptionMessage.setBizExceptionMessage("MLRL00213",
								new String[] { "여신총괄지원팀" });
						existInvalidElement = true;
					}

					if (existInvalidElement) {
						bizExceptionMessage.throwBizException();
					}
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("clsg_chck_yn")), "Y")) {
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신창구시재마감조회  입력값 세팅
				// =============================================================================
				iLonWickRdmyClsgInq = new LData();

				iLonWickRdmyClsgInq.setString("clsg_dt", sNowDt);
				iLonWickRdmyClsgInq.setString("prc_orz_cd", sAcnyOrzCd);

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신계약기본원장수납검증 - 여신창구시재마감조회  입력값 세팅");
					LLog.debug.println(iLonWickRdmyClsgInq);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonWickRdmyClsgInq);
				rLonWickRdmyClsgInq = lonWickRdmyClsgEbc
						.retvLonWickRdmyClsg(iLonWickRdmyClsgInq); //##여신창구시재마감조회

				// =============================================================================
				// ######### CodeValidationBlock ##마감완료로 수납불가합니다.
				// =============================================================================
				{
					LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
					boolean existInvalidElement = false;

					if (KLDataConvertUtil.equals(
							rLonWickRdmyClsgInq.getString("clsg_yn"), "Y")) {
						bizExceptionMessage.setBizExceptionMessage("MLRL00478",
								new String[] { "마감완료" });
						existInvalidElement = true;
					}

					if (existInvalidElement) {
						bizExceptionMessage.throwBizException();
					}
				}
			} catch (LBizNotFoundException e) {

				// =============================================================================
				// ######### GeneralCodeBlock ##마감자료없음 로그출력
				// =============================================================================
				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■ ACSD_여신계약기본원장수납검증 - 마감자료없음 로그출력");
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("bde_trs_chck_yn")), "Y")) {

			//일괄이체중 무응답건 체크
			{

				// =============================================================================
				// ######### GeneralCodeBlock ##여신일괄이체내역[대출번호]건수조회  입력값 세팅
				// =============================================================================
				//일괄이체참조번호20 = 대출번호12 + 대출일련번호5
				iLonBdeTrsHisLnNoNcnInq.setString("ln_no",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getString("ln_no")));
				iLonBdeTrsHisLnNoNcnInq.setLong("ln_seq",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getLong("ln_seq")));

				iLonBdeTrsHisLnNoNcnInq.setString("bde_trs_ss_cd", "01"); //이체중

				LLog.debug
						.println("■■■ ACSD_여신계약기본원장수납검증 - 여신일괄이체내역lnNo건수조회 START");
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonBdeTrsHisLnNoNcnInq);
				rLonBdeTrsHisLnNoNcnInq = lonBdeTrsHisEbc
						.retvLonBdeTrsHisByLnNo(iLonBdeTrsHisLnNoNcnInq); //##여신일괄이체내역[대출번호]건수조회

				// =============================================================================
				// ######### CodeValidationBlock ##일괄이체중 무응답건 수납불가 오류처리 : 이체중이므로 수납불가합니다.
				// =============================================================================
				{
					LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
					boolean existInvalidElement = false;

					LLog.debug.println("■■■ ACSD_여신계약기본원장수납검증 일괄이체중 확인");

					//이체중이므로 수납불가합니다.
					if (rLonBdeTrsHisLnNoNcnInq.getLong("inq_ncn") > 0) {
						bizExceptionMessage.setBizExceptionMessage("MLRL00214",
								new String[] { "" });
						existInvalidElement = true;
					}

					if (existInvalidElement) {
						bizExceptionMessage.throwBizException();
					}
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("imdt_trs_chck_yn")), "Y")) {

			//즉시이체중 무응답건 체크
			{

				// =============================================================================
				// ######### GeneralCodeBlock ##여신즉시이체내역[대출번호]건수조회  입력값 세팅
				// =============================================================================
				// [i여신즉시이체내역대출번호건수조회]  

				iLonImdtTrsHisLnNoNcnInq.setString("ln_no",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getString("ln_no")));
				iLonImdtTrsHisLnNoNcnInq.setLong("ln_seq",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getLong("ln_seq")));

				iLonImdtTrsHisLnNoNcnInq.setString("trs_ss_cd", "01"); //이체중

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신계약기본원장수납검증 - 여신즉시이체내역lnNo건수조회 START");
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonImdtTrsHisLnNoNcnInq);
				rLonImdtTrsHisLnNoNcnInq = lonImdtTrsHisEbc
						.retvLonImdtTrsHisByLnNo(iLonImdtTrsHisLnNoNcnInq); //##여신즉시이체내역[대출번호]건수조회

				// =============================================================================
				// ######### CodeValidationBlock ##이체내역중 무응답건 수납불가 오류처리 : 이체중이므로 수납불가합니다.
				// =============================================================================
				{
					LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
					boolean existInvalidElement = false;

					if (LLog.debug.isEnabled()) {
						LLog.debug.println("■■■ ACSD_여신계약기본원장수납검증 즉시이체 확인");
					}

					//이체중이므로 수납불가합니다.
					if (rLonImdtTrsHisLnNoNcnInq.getLong("inq_ncn") > 0) {
						bizExceptionMessage.setBizExceptionMessage("MLRL00214",
								new String[] { "" });
						existInvalidElement = true;
					}

					if (existInvalidElement) {
						bizExceptionMessage.throwBizException();
					}
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("ig_daw_rsv_chck_yn")), "Y")) {

			// =============================================================================
			// ######### GeneralCodeBlock ##여신예약이체내역[대출번호]목록조회  입력값 세팅
			// =============================================================================
			iLonRsvTrsHisLnNoListInq = new LData();
			iLonRsvTrsHisLnNoListInq.setString("ln_no",
					(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
							.getString("ln_no")));
			iLonRsvTrsHisLnNoListInq.setLong("ln_seq",
					(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
							.getLong("ln_seq")));
			iLonRsvTrsHisLnNoListInq.setString("rsv_trs_dt", sNowDt);
			iLonRsvTrsHisLnNoListInq.setString("rsv_trs_dv_cd", "5"); //통합입출금접수
			iLonRsvTrsHisLnNoListInq.setString("rsv_rsl_dv_cd", "1"); //예약

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ 여신예약이체내역대출번호목록조회");
				LLog.debug.println(iLonRsvTrsHisLnNoListInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iLonRsvTrsHisLnNoListInq);
			rLonRsvTrsHisLnNoListInq = lonRsvTrsHisEbc
					.retvLstLonRsvTrsHisByLnNo(iLonRsvTrsHisLnNoListInq); //##여신예약이체내역[대출번호]목록조회

			// =============================================================================
			// ######### CodeValidationBlock ##통합입출금 예약중인건 수납불가 오류처리 : 통합입출금 예약중이므로 수납불가합니다.
			// =============================================================================
			{
				LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
				boolean existInvalidElement = false;

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신계약기본원장수납검증 - 통합입출금 예약중인건 수납불가");
				}

				//통합입출금 예약중이므로 수납불가합니다.
				if (rLonRsvTrsHisLnNoListInq.getDataCount() > 0) {
					bizExceptionMessage.setBizExceptionMessage("MLRL00478",
							new String[] { "통합입출금 예약중" });
					existInvalidElement = true;
				}

				if (existInvalidElement) {
					bizExceptionMessage.throwBizException();
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("exln_ln_chck_yn")), "Y")) {

			//대환중 체크
			{

				// =============================================================================
				// ######### GeneralCodeBlock ##대환대출내역[대환대출번호]목록조회  입력값 세팅
				// =============================================================================
				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신계약기본원장수납검증 - 대환대출내역exlnLnNo목록조회 INPUT SET");
				}
				iExlnLnHisExlnLnNoListInq.setString("exln_ln_no",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getString("ln_no")));
				iExlnLnHisExlnLnNoListInq.setLong("exln_ln_seq",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getLong("ln_seq")));
				iExlnLnHisExlnLnNoListInq.setString("lon_csl_prs_ss_cd", "10"); //진행중
				iExlnLnHisExlnLnNoListInq.setString("pn_exec_yn", "Y");
				//[i대환대출내역대환대출번호목록조회].[대출접수여부] = "Y";	//신규추가됨
				//[i대환대출내역대환대출번호목록조회].[상품확정여부] = "Y";	//신규추가됨

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신계약기본원장수납검증 - 대환대출내역exlnLnNo목록조회 START");
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iExlnLnHisExlnLnNoListInq);
				rExlnLnHisExlnLnNoListInq = exlnLnHisEbc
						.retvLstExlnLnHisByExlnLnNo(iExlnLnHisExlnLnNoListInq); //##대환대출내역[대환대출번호]목록조회
				for (int inx = 0, inxLoopSize = rExlnLnHisExlnLnNoListInq
						.getDataCount(); inx < inxLoopSize; inx++) {
					LData tExlnLnHis = rExlnLnHisExlnLnNoListInq.getLData(inx);
					LProtocolInitializeUtil
							.primitiveLMultiInitialize(tExlnLnHis);

					// =============================================================================
					// ######### GeneralCodeBlock ##여신계약기본[여신상담ID]목록조회  입력값 세팅
					// =============================================================================
					iLonCnrBasLonCslIdListInq.setString("lon_csl_id",
							tExlnLnHis.getString("lon_csl_id"));

					if (LLog.debug.isEnabled()) {
						LLog.debug
								.println("■■■ ACSD_여신계약기본원장수납검증 - 여신계약기본lonCslId목록조회 START");
					}
					LProtocolInitializeUtil
							.primitiveLMultiInitialize(iLonCnrBasLonCslIdListInq);
					rLonCnrBasLonCslIdListInq = lonCnrBasEbc
							.retvLstLonCnrBasByLonCslId(iLonCnrBasLonCslIdListInq); //##여신계약기본[여신상담ID]목록조회
					for (int jnx = 0, jnxLoopSize = rLonCnrBasLonCslIdListInq
							.getDataCount(); jnx < jnxLoopSize; jnx++) {
						LData tLonCnrBasHis = rLonCnrBasLonCslIdListInq
								.getLData(jnx);
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(tLonCnrBasHis);

						if (KLDataConvertUtil.equals(
								tLonCnrBasHis.getString("ln_ss_cd"), "09") //대출상태코드 - 취소
						) {

							// =============================================================================
							// ######### ExceptionCodeBlock ##대환중인 건 오류처리 : 대환중이므로 수납불가
							// =============================================================================
							{
								//대환중이므로 수납불가합니다.
								throw new BizException("MLRL00215");
							}
						} else {

							// =============================================================================
							// ######### GeneralCodeBlock ##여신즉시이체내역[이체상태코드2]목록조회  입력값 세팅
							// =============================================================================
							iLonImdtTrsHisTrsSsCd2ListInq.setString("ln_no",
									tLonCnrBasHis.getString("ln_no"));
							iLonImdtTrsHisTrsSsCd2ListInq.setLong("ln_seq",
									tLonCnrBasHis.getLong("ln_seq"));
							iLonImdtTrsHisTrsSsCd2ListInq.setString(
									"trs_ss_cd1", "01"); //이체중
							iLonImdtTrsHisTrsSsCd2ListInq.setString(
									"trs_ss_cd2", "06"); //계산오류

							if (LLog.debug.isEnabled()) {
								LLog.debug
										.println("■■■ ACSD_여신계약기본원장수납검증 - 여신즉시이체내역trsSsCd2목록조회 START");
							}
							LProtocolInitializeUtil
									.primitiveLMultiInitialize(iLonImdtTrsHisTrsSsCd2ListInq);
							rLonImdtTrsHisTrsSsCd2ListInq = lonImdtTrsHisEbc
									.retvLstLonImdtTrsHisByTrsSsCd2(iLonImdtTrsHisTrsSsCd2ListInq); //##여신즉시이체내역[이체상태코드2]목록조회

							// =============================================================================
							// ######### CodeValidationBlock ##대환중인 건 오류처리 : 대환중이므로 수납불가합니다. 
							// =============================================================================
							{
								LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
								boolean existInvalidElement = false;

								//대환중이므로 수납불가합니다.
								if (rLonImdtTrsHisTrsSsCd2ListInq
										.getDataCount() > 0) {
									bizExceptionMessage.setBizExceptionMessage(
											"MLRL00215", new String[] { "" });
									existInvalidElement = true;
								}

								if (existInvalidElement) {
									bizExceptionMessage.throwBizException();
								}
							}
						}
					}
				}
			}
		}
		if (KLDataConvertUtil.equals((((LData) iLonCnrBasLdgRcvVrf
				.get("chckItm")).getString("ch_rsv_chck_yn")), "Y")) {

			//기일내 변경예약건 체크
			{

				// =============================================================================
				// ######### GeneralCodeBlock ##CRP조건변경수납신청내역[CRP조건변경상태코드]건수조회  입력값 세팅
				// =============================================================================
				iCrpCondChRcvReqHisCrpCondChSsCdNcnInq.setString("ln_no",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getString("ln_no")));
				iCrpCondChRcvReqHisCrpCondChSsCdNcnInq.setLong("ln_seq",
						(((LData) iLonCnrBasLdgRcvVrf.get("basMtr"))
								.getLong("ln_seq")));

				iCrpCondChRcvReqHisCrpCondChSsCdNcnInq.setString(
						"crp_cond_ch_ss_cd", "1"); //예약중

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신계약기본원장수납검증 - CRP조건변경수납신청내역crpCondChSsCd건수조회 START");
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iCrpCondChRcvReqHisCrpCondChSsCdNcnInq);
				rCrpCondChRcvReqHisCrpCondChSsCdNcnInq = crpCondChRcvReqHisEbc
						.retvCrpCondChRcvReqHisByCrpCondChSsCd(iCrpCondChRcvReqHisCrpCondChSsCdNcnInq); //##CRP조건변경수납신청내역[CRP조건변경상태코드]건수조회

				// =============================================================================
				// ######### CodeValidationBlock ##기일내변경예약건 수납불가 오류처리 : 연체콜센타 기일내변경수납 예약중이므로 수납불가합니다.
				// =============================================================================
				{
					LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
					boolean existInvalidElement = false;

					if (LLog.debug.isEnabled()) {
						LLog.debug.println("■■■ ACSD_여신계약기본원장수납검증 기일내변경예약건 확인");
					}

					//연체콜센타 기일내변경수납 예약중이므로 수납불가합니다.
					if (rCrpCondChRcvReqHisCrpCondChSsCdNcnInq
							.getLong("inq_ncn") > 0) {
						bizExceptionMessage.setBizExceptionMessage("MLRL00216",
								new String[] { "" });
						existInvalidElement = true;
					}

					if (existInvalidElement) {
						bizExceptionMessage.throwBizException();
					}
				}
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과로그출력
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_여신계약기본원장수납검증 END - 정상");
		}
	}

	/**
	 * 
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055114
	 * @logicalName   여신고객정보조회
	 * @param LData iLonCsImInq i여신고객정보조회
	 * @return LData rLonCsImInq r여신고객정보조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_여신고객정보조회
	 * 
	 */
	public LData retvLonCsIm(LData iLonCsImInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rLonCsImInq = new LData(); //# r여신고객정보조회
											//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iLonCnrBasLonRcpNoListInq = new LData(); //# i여신계약기본여신접수번호목록조회
		LMultiData rLonCnrBasLonRcpNoListInq = new LMultiData(); //# r여신계약기본여신접수번호목록조회
		LData iVtlAcoNoInqDvListInq = new LData(); //# i가상계좌번호조회구분목록조회
		LMultiData rVtlAcoNoInqDvListInq = new LMultiData(); //# r가상계좌번호조회구분목록조회
		LData iLonCnrBasLnNoListInq = new LData(); //# i여신계약기본대출번호목록조회
		LMultiData rLonCnrBasLnNoListInq = new LMultiData(); //# r여신계약기본대출번호목록조회
		LData iCsNoOmBasInq = new LData(); //# i고객번호조직원기본조회
		LData rCsNoOmBasInq = new LData(); //# r고객번호조직원기본조회
		LData iIgCsImInq = new LData(); //# i통합고객정보조회
		LData rIgCsImInq = new LData(); //# r통합고객정보조회
		LData iIdlCsSsInq = new LData(); //# i개인고객상태조회
		LData rIdlCsSsInq = new LData(); //# r개인고객상태조회
		LData iGpCsIgCsNoInq = new LData(); //# i단체고객통합고객번호조회
		LData rGpCsIgCsNoInq = new LData(); //# r단체고객통합고객번호조회
		LData iSopIdqHisInq = new LData(); //# i중지부적격내역조회
		LMultiData rSopIdqHisInq = new LMultiData(); //# r중지부적격내역조회
		//#호출 컴포넌트 초기화
		LonCnrBasEbc lonCnrBasEbc = new LonCnrBasEbc(); //# 여신계약기본Ebi
		VtlAcoMgtIbc vtlAcoMgtIbc = new VtlAcoMgtIbc(); //# 가상계좌관리Ibi
		OmBasEbc omBasEbc = new OmBasEbc(); //# 조직원기본Ebi
		CsCommImIbc csCommImIbc = new CsCommImIbc(); //# 고객공통정보Ibi
		IdlCsIbc idlCsIbc = new IdlCsIbc(); //# 개인고객Ibi
		GpCsIbc gpCsIbc = new GpCsIbc(); //# 단체고객Ibi
		IdqCsMgtCpbc idqCsMgtCpbc = new IdqCsMgtCpbc(); //# 부적격고객관리Cpbi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 설정
		// =============================================================================
		boolean bLnNoNinYn = KStringUtil.trimNisEmpty(iLonCsImInq
				.getString("ln_no")); //#b대출번호미입력여부
		boolean bVtlAcoBnkCdNinYn = KStringUtil.trimNisEmpty(iLonCsImInq
				.getString("vtl_aco_bnk_cd")); //#b가상계좌은행코드미입력여부
		boolean bVtlAcoNoNinYn = KStringUtil.trimNisEmpty(iLonCsImInq
				.getString("vtl_aco_no")); //#b가상계좌번호미입력여부
		boolean bLonRcpNoNinYn = KStringUtil.trimNisEmpty(iLonCsImInq
				.getString("lon_rcp_no")); //#b여신접수번호미입력여부
		boolean bIgCsNoNinYn = KStringUtil.trimNisEmpty(iLonCsImInq
				.getString("ig_cs_no")); //#b통합고객번호미입력여부

		LData tiLonCsImIn = new LData(); //#ti여신고객정보입력
		// tiLonCsImIn <- iLonCsImInq;
		tiLonCsImIn.setString("ig_cs_no", iLonCsImInq.getString("ig_cs_no")); //#통합고객번호
		tiLonCsImIn.setString("ln_no", iLonCsImInq.getString("ln_no")); //#대출번호
		tiLonCsImIn.setLong("ln_seq", iLonCsImInq.getLong("ln_seq")); //#대출일련번호
		tiLonCsImIn.setString("vtl_aco_bnk_cd",
				iLonCsImInq.getString("vtl_aco_bnk_cd")); //#가상계좌은행코드
		tiLonCsImIn
				.setString("vtl_aco_no", iLonCsImInq.getString("vtl_aco_no")); //#가상계좌번호
		tiLonCsImIn
				.setString("lon_rcp_no", iLonCsImInq.getString("lon_rcp_no")); //#여신접수번호

		// =============================================================================
		// ######### CodeValidationBlock ##입력값 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			//--------------------------------------------------------------------------
			// 입력사항 검증
			//--------------------------------------------------------------------------
			//필수입력사항 체크

			if (bIgCsNoNinYn && bLnNoNinYn && bVtlAcoNoNinYn && bLonRcpNoNinYn) {
				bizExceptionMessage.setBizExceptionMessage("MLRL00258",
						new String[] { "대출번호/여신접수번호/가상계좌번호/통합고객번호" });
				existInvalidElement = true;
			} else if (bIgCsNoNinYn && bLnNoNinYn && !(bVtlAcoNoNinYn)
					&& bLonRcpNoNinYn) {
				if (bVtlAcoBnkCdNinYn) {
					bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
							new String[] { "가상계좌은행코드" });
					existInvalidElement = true;
				}
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과DTO 초기화
		// =============================================================================
		LProtocolInitializeUtil.primitiveLMultiInitialize(rLonCsImInq);
		if (KLDataConvertUtil.equals(bLnNoNinYn, true)) {
			if (KLDataConvertUtil.equals(bLonRcpNoNinYn, false)) {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신계약기본[여신접수번호]목록조회  입력값 세팅
				// =============================================================================
				iLonCnrBasLonRcpNoListInq.setString("lon_rcp_no",
						tiLonCsImIn.getString("lon_rcp_no"));
				iLonCnrBasLonRcpNoListInq.setString("ln_ss_cd", "00");

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신고객정보조회 - 여신계약기본lonRcpNo목록조회  입력값 세팅");
					LLog.debug.println(iLonCnrBasLonRcpNoListInq);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonCnrBasLonRcpNoListInq);
				rLonCnrBasLonRcpNoListInq = lonCnrBasEbc
						.retvLstLonCnrBasByLonRcpNo(iLonCnrBasLonRcpNoListInq); //##여신계약기본[여신접수번호]목록조회
				if (rLonCnrBasLonRcpNoListInq.getDataCount() <= 0) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 여신접수번호 조회결과가 없습니다.
					// =============================================================================
					{
						throw new BizException("MZZZ00161",
								new String[] { "여신접수번호" });
					}
				}

				// =============================================================================
				// ######### GeneralCodeBlock ##여신계약기본[여신접수번호]목록조회  결과값 맵핑
				// =============================================================================
				tiLonCsImIn.setString("ln_no",
						rLonCnrBasLonRcpNoListInq.getString("ln_no", 0));
				tiLonCsImIn.setLong("ln_seq",
						rLonCnrBasLonRcpNoListInq.getLong("ln_seq", 0));
				tiLonCsImIn.setString("ig_cs_no",
						rLonCnrBasLonRcpNoListInq.getString("ig_cs_no", 0));
				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신고객정보조회 - 여신계약기본lonRcpNo목록조회  결과값 맵핑");
					LLog.debug.println(tiLonCsImIn);
				}
			} else if (KLDataConvertUtil.equals(bVtlAcoNoNinYn, false)) {

				// =============================================================================
				// ######### GeneralCodeBlock ##가상계좌번호[조회구분]목록조회  입력값 세팅 3
				// =============================================================================
				iVtlAcoNoInqDvListInq.setString("inq_dv", "3"); //가상계좌번호
				iVtlAcoNoInqDvListInq.setString("vtl_aco_bnk_cd",
						tiLonCsImIn.getString("vtl_aco_bnk_cd"));
				iVtlAcoNoInqDvListInq.setString("vtl_aco_no",
						tiLonCsImIn.getString("vtl_aco_no"));

				iVtlAcoNoInqDvListInq.setString("daw_lob_dv_cd", "L");
				iVtlAcoNoInqDvListInq.setString("daw_dty_dv_cd", "LRL");
				iVtlAcoNoInqDvListInq.setString("vtl_aco_ss_cd", "01"); //가상계좌상태코드(01 : 정상)

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신고객정보조회 - 가상계좌번호inqDv목록조회  입력값 세팅");
					LLog.debug.println(iVtlAcoNoInqDvListInq);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iVtlAcoNoInqDvListInq);
				rVtlAcoNoInqDvListInq = vtlAcoMgtIbc
						.retvLstVtlAcoNoByInqDv(iVtlAcoNoInqDvListInq); //##가상계좌번호[조회구분]목록조회
				if (rVtlAcoNoInqDvListInq.getDataCount() <= 0) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##오류처리 : 발급된 가상계좌번호가 없습니다.
					// =============================================================================
					{
						throw new BizException("MARP00174", new String[] { "" });
					}
				}

				// =============================================================================
				// ######### GeneralCodeBlock ##가상계좌번호[조회구분]목록조회  결과값 맵핑
				// =============================================================================
				tiLonCsImIn.setString("ln_no",
						rVtlAcoNoInqDvListInq.getString("daw_rre_no1", 0));
				if (KStringUtil.trimNisEmpty(rVtlAcoNoInqDvListInq.getString(
						"daw_rre_no1", 0))) {
					tiLonCsImIn.setLong("ln_seq", 0);
				} else {
					tiLonCsImIn.setLong("ln_seq", KTypeConverter
							.parseTo_long(rVtlAcoNoInqDvListInq.getString(
									"daw_rre_no2", 0)));
				}

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신고객정보조회 - 가상계좌번호inqDv목록조회  결과값 맵핑");
					LLog.debug.println(rVtlAcoNoInqDvListInq);
				}
			}
		} else {
		}
		if (!(KStringUtil.trimNisEmpty(tiLonCsImIn.getString("ln_no")))
				&& (KStringUtil.trimNisEmpty(tiLonCsImIn
						.getString("lon_rcp_no")) || KStringUtil
						.trimNisEmpty(tiLonCsImIn.getString("ig_cs_no")))) {

			// =============================================================================
			// ######### GeneralCodeBlock ##여신계약기본[대출번호]목록조회  입력값 세팅
			// =============================================================================
			iLonCnrBasLnNoListInq.setString("ln_no",
					tiLonCsImIn.getString("ln_no"));

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_여신고객정보조회 - 여신계약기본lnNo목록조회  입력값 세팅");
				LLog.debug.println(iLonCnrBasLnNoListInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iLonCnrBasLnNoListInq);
			rLonCnrBasLnNoListInq = lonCnrBasEbc
					.retvLstLonCnrBasByLnNo(iLonCnrBasLnNoListInq); //##여신계약기본[대출번호]목록조회
			if (rLonCnrBasLnNoListInq.getDataCount() <= 0) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 여신계약 조회결과가 없습니다.
				// =============================================================================
				{
					throw new BizException("MZZZ00161", new String[] { "여신계약" });
				}
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##입력값 변경 - 통합고객번호, 여신접수번호
			// =============================================================================
			tiLonCsImIn.setString("ig_cs_no",
					rLonCnrBasLnNoListInq.getString("ig_cs_no", 0));
			tiLonCsImIn.setString("lon_rcp_no",
					rLonCnrBasLnNoListInq.getString("lon_rcp_no", 0));

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_여신고객정보조회 - 입력값 변경 - 통합고객번호, 여신접수번호");
				LLog.debug.println(rLonCnrBasLnNoListInq);
			}
		}
		if (!(KStringUtil.trimNisEmpty(tiLonCsImIn.getString("ln_no")))) {

			// =============================================================================
			// ######### GeneralCodeBlock ##가상계좌번호[조회구분]목록조회  입력값 세팅 2
			// =============================================================================
			iVtlAcoNoInqDvListInq.setString("inq_dv", "2");
			iVtlAcoNoInqDvListInq.setString("daw_rre_no_dv_cd",
					DawRreNoDvCdConst.LN_NO.getCode());
			iVtlAcoNoInqDvListInq.setString("daw_rre_no1",
					tiLonCsImIn.getString("ln_no"));
			iVtlAcoNoInqDvListInq.setString("daw_rre_no2",
					KTypeConverter.toString(tiLonCsImIn.getLong("ln_seq")));
			iVtlAcoNoInqDvListInq.setString("vtl_aco_ss_cd", "01"); //가상계좌상태코드(01 : 정상)

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_여신고객정보조회 - 가상계좌번호inqDv목록조회  입력값 세팅 2");
				LLog.debug.println(iVtlAcoNoInqDvListInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iVtlAcoNoInqDvListInq);
			rVtlAcoNoInqDvListInq = vtlAcoMgtIbc
					.retvLstVtlAcoNoByInqDv(iVtlAcoNoInqDvListInq); //##가상계좌번호[조회구분]목록조회
			if (rVtlAcoNoInqDvListInq.getDataCount() > 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##가상계좌번호 입력
				// =============================================================================
				tiLonCsImIn.setString("vtl_aco_bnk_cd",
						rVtlAcoNoInqDvListInq.getString("vtl_aco_bnk_cd", 0));
				tiLonCsImIn.setString("vtl_aco_no",
						rVtlAcoNoInqDvListInq.getString("vtl_aco_no", 0));
				rLonCsImInq.setString("vtl_aco_iss_id",
						rVtlAcoNoInqDvListInq.getString("vtl_aco_iss_id", 0));

				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■ ACSD_여신고객정보조회 - 가상계좌번호 입력");
					LLog.debug.println(tiLonCsImIn);
				}
			} else {

				// =============================================================================
				// ######### GeneralCodeBlock ##가상계좌번호 입력 null
				// =============================================================================
				tiLonCsImIn.setString("vtl_aco_bnk_cd", "");
				tiLonCsImIn.setString("vtl_aco_no", "");
				rLonCsImInq.setString("vtl_aco_iss_id", "");

				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■ ACSD_여신고객정보조회 - 가상계좌번호 입력 null");
					LLog.debug.println(tiLonCsImIn);
				}
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 입력 - 대출정보
		// =============================================================================
		// rLonCsImInq <- tiLonCsImIn;
		rLonCsImInq.setString("ig_cs_no", tiLonCsImIn.getString("ig_cs_no")); //#통합고객번호
		rLonCsImInq.setString("ln_no", tiLonCsImIn.getString("ln_no")); //#대출번호
		rLonCsImInq.setLong("ln_seq", tiLonCsImIn.getLong("ln_seq")); //#대출일련번호
		rLonCsImInq.setString("vtl_aco_bnk_cd",
				tiLonCsImIn.getString("vtl_aco_bnk_cd")); //#가상계좌은행코드
		rLonCsImInq
				.setString("vtl_aco_no", tiLonCsImIn.getString("vtl_aco_no")); //#가상계좌번호
		rLonCsImInq
				.setString("lon_rcp_no", tiLonCsImIn.getString("lon_rcp_no")); //#여신접수번호

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_여신고객정보조회 - 결과값 입력 - 대출정보");
			LLog.debug.println(rLonCsImInq);
		}
		try {

			// =============================================================================
			// ######### GeneralCodeBlock ##고객번호조직원기본조회  입력값 세팅
			// =============================================================================
			iCsNoOmBasInq.setString("ig_cs_no",
					tiLonCsImIn.getString("ig_cs_no"));
			iCsNoOmBasInq.setString("dsms_yn", "1"); //위촉

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_여신고객정보조회 - 고객번호조직원기본조회  입력값 세팅");
				LLog.debug.println(iCsNoOmBasInq);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(iCsNoOmBasInq);
			rCsNoOmBasInq = omBasEbc.retvCsNoOmBas(iCsNoOmBasInq); //##고객번호조직원기본조회

			// =============================================================================
			// ######### GeneralCodeBlock ##결과값 입력 - 조직원번호
			// =============================================================================
			rLonCsImInq.setString("om_no", rCsNoOmBasInq.getString("om_no"));

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_여신고객정보조회 - 결과값 입력 - 조직원번호");
				LLog.debug.println(rCsNoOmBasInq);
			}
		} catch (LBizNotFoundException e) {

			// =============================================================================
			// ######### GeneralCodeBlock ##결과값 입력 - 조직원번호2
			// =============================================================================
			rLonCsImInq.setString("om_no", "");

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_여신고객정보조회 - 결과값 입력 - 조직원번호2");
				LLog.debug.println(rCsNoOmBasInq);
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##통합고객정보조회  입력값 세팅
		// =============================================================================
		iIgCsImInq.setString("ig_cs_no", tiLonCsImIn.getString("ig_cs_no"));

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_여신고객정보조회 - 통합고객정보조회  입력값 세팅");
			LLog.debug.println(iIgCsImInq);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(iIgCsImInq);
		rIgCsImInq = csCommImIbc.retvIgCsIm(iIgCsImInq); //##통합고객정보조회

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 입력 - 개인단체구분코드
		// =============================================================================
		rLonCsImInq.setString("idl_gp_dv_cd",
				rIgCsImInq.getString("idl_gp_dv_cd"));
		if (KLDataConvertUtil.equals(rIgCsImInq.getString("idl_gp_dv_cd"),
				IdlGpDvCdConst.IDL.getCode())) {

			// =============================================================================
			// ######### GeneralCodeBlock ##통합고객정보조회  결과값 맵핑
			// =============================================================================
			rLonCsImInq.setString("cs_nm", rIgCsImInq.getString("cs_nm"));

			if (KLDataConvertUtil.equals(
					rIgCsImInq.getString("rlnm_cf_no_kd_cd"),
					RlnmCfNoKdCdConst.RGNO.getCode())) {
				rLonCsImInq.setString("rgno",
						rIgCsImInq.getString("rlnm_cf_no"));
			} else if (KLDataConvertUtil.equals(
					rIgCsImInq.getString("rlnm_cf_no_kd_cd"),
					RlnmCfNoKdCdConst.BRNO.getCode())) {
				rLonCsImInq.setString("brno",
						rIgCsImInq.getString("rlnm_cf_no"));
			} else {
				rLonCsImInq.setString("rgno",
						rIgCsImInq.getString("rlnm_cf_no"));
			}
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##개인고객상태조회  입력값 세팅
				// =============================================================================
				// [i개인고객상태조회]  

				iIdlCsSsInq = new LData();
				rIdlCsSsInq = new LData();

				iIdlCsSsInq.setString("ig_cs_no",
						tiLonCsImIn.getString("ig_cs_no"));

				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■ ACSD_여신고객정보조회 - 개인고객상태조회  입력값 세팅");
					LLog.debug.println(iIdlCsSsInq);
				}
				LProtocolInitializeUtil.primitiveLMultiInitialize(iIdlCsSsInq);
				rIdlCsSsInq = idlCsIbc.retvIdlCsSs(iIdlCsSsInq); //##개인고객상태조회

				// =============================================================================
				// ######### GeneralCodeBlock ##개인고객상태조회  결과값 맵핑
				// =============================================================================
				rLonCsImInq.setString("idl_cs_ss_cd",
						rIdlCsSsInq.getString("idl_cs_ss_cd"));
				rLonCsImInq.setString("idl_cs_ss_nm", KIntegrationCodeUtil
						.getIgCdValiValNm("IDL_CS_SS_CD",
								rIdlCsSsInq.getString("idl_cs_ss_cd")));
			} catch (LBizException e) {

				// =============================================================================
				// ######### GeneralCodeBlock ##개인고객상태조회 오류출력 PASS
				// =============================================================================
				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_여신고객정보조회 - 개인고객상태조회 오류출력 PASS:"
									+ KExceptionUtil.getMessage(e));
				}
			}
		} else if (KLDataConvertUtil.equals(
				rIgCsImInq.getString("idl_gp_dv_cd"),
				IdlGpDvCdConst.GP.getCode())) {

			// =============================================================================
			// ######### GeneralCodeBlock ##단체고객조회  입력값 세팅
			// =============================================================================
			iGpCsIgCsNoInq.setString("ig_cs_no",
					tiLonCsImIn.getString("ig_cs_no"));
			LProtocolInitializeUtil.primitiveLMultiInitialize(iGpCsIgCsNoInq);
			rGpCsIgCsNoInq = gpCsIbc.retvGpCs(iGpCsIgCsNoInq); //##단체고객조회

			// =============================================================================
			// ######### GeneralCodeBlock ##단체고객조회  결과값 맵핑
			// =============================================================================
			// [r단체고객통합고객번호조회]
			rLonCsImInq.setString("cs_nm", rIgCsImInq.getString("cs_nm"));
			rLonCsImInq.setString("brno", (((LData) rGpCsIgCsNoInq
					.get("gpCsBasImDto")).getString("brno")));
			rLonCsImInq.setString("jrno", (((LData) rGpCsIgCsNoInq
					.get("gpCsBasImDto")).getString("jrno")));
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##중지부적격내역조회  입력값 세팅
		// =============================================================================
		// [i중지부적격내역조회]
		iSopIdqHisInq.setString("ig_cs_no", tiLonCsImIn.getString("ig_cs_no"));
		iSopIdqHisInq.setString("idq_impi_dv_cd", "1");
		LProtocolInitializeUtil.primitiveLMultiInitialize(iSopIdqHisInq);
		rSopIdqHisInq = idqCsMgtCpbc.retvSopIdqHis(iSopIdqHisInq); //##중지부적격내역조회
		for (int inx = 0, inxLoopSize = rSopIdqHisInq.getDataCount(); inx < inxLoopSize; inx++) {
			LData tSopIdqHisInq = rSopIdqHisInq.getLData(inx);
			LProtocolInitializeUtil.primitiveLMultiInitialize(tSopIdqHisInq);

			if (KLDataConvertUtil.equals(tSopIdqHisInq.getString("idq_rsn_cd"),
					"08")) {

				// =============================================================================
				// ######### GeneralCodeBlock ##결과값 입력
				// =============================================================================
				rLonCsImInq.setString("idq_rsn_cd", "08");

				// =============================================================================
				// ######### GeneralCodeBlock ##break
				// =============================================================================
				break;
			}
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rLonCsImInq);
		return rLonCsImInq;
	}

	/**
	 * 여신원리금계산임시등록
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055104
	 * @logicalName   여신원리금계산임시등록
	 * @param LData iLonPcitClcTempReg i여신원리금계산임시등록
	 * @return LData rLonPcitClcTempReg r여신원리금계산임시등록
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_여신원리금계산임시등록
	 * 
	 */
	public LData regtLonPcitClcTemp(LData iLonPcitClcTempReg) throws LException {
		//#Return 변수 선언 및 초기화
		LData rLonPcitClcTempReg = new LData(); //# r여신원리금계산임시등록
												//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData rDtmGnoInq = new LData(); //# r일시채번조회
		LData iLonPcitClcHisTempReg = new LData(); //# i여신원리금계산내역임시등록
		long rLonPcitClcHisTempReg = 0; //# r여신원리금계산내역임시등록
		//#호출 컴포넌트 초기화
		LonCnrBasRcvPatEbc lonCnrBasRcvPatEbc = new LonCnrBasRcvPatEbc(); //# 여신계약기본수납파트Ebi
		LonPcitClcTempEbc lonPcitClcTempEbc = new LonPcitClcTempEbc(); //# 여신원리금계산임시Ebi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 설정
		// =============================================================================
		//[i여신원리금계산임시등록].[원리금계산구간목록]* [t원리금계산구간목록];
		LMultiData tPcitClcScList = new LMultiData(); //#t원리금계산구간목록
		// tPcitClcScList <- (LMultiData)iLonPcitClcTempReg.get("pcitClcScList");
		tPcitClcScList = new LMultiData();
		for (int anx = 0, anxLoopSize = ((LMultiData) iLonPcitClcTempReg
				.get("pcitClcScList")).getDataCount(); anx < anxLoopSize; anx++) {
			LData ta__tPcitClcScList = new LData(); //#ta__t원리금계산구간목록
			LData ta_PcitClcScList = ((LMultiData) iLonPcitClcTempReg
					.get("pcitClcScList")).getLData(anx); //#ta_원리금계산구간목록
			LProtocolInitializeUtil.primitiveLMultiInitialize(ta_PcitClcScList);
			ta__tPcitClcScList.setLong("ln_seq",
					ta_PcitClcScList.getLong("ln_seq")); //#대출일련번호
			ta__tPcitClcScList.setString("pcit_rcv_clc_dv_cd",
					ta_PcitClcScList.getString("pcit_rcv_clc_dv_cd")); //#원리금수납계산구분코드
			ta__tPcitClcScList.setString("pcit_rcv_clc_dv_cd_nm",
					ta_PcitClcScList.getString("pcit_rcv_clc_dv_cd_nm")); //#원리금수납계산구분코드명
			ta__tPcitClcScList.setBigDecimal("rckg_amt",
					ta_PcitClcScList.getBigDecimal("rckg_amt")); //#기산금액
			ta__tPcitClcScList.setBigDecimal("ap_itt",
					ta_PcitClcScList.getBigDecimal("ap_itt")); //#적용금리
			ta__tPcitClcScList.setString("clc_st_dt",
					ta_PcitClcScList.getString("clc_st_dt")); //#계산시작일자
			ta__tPcitClcScList.setString("clc_ed_dt",
					ta_PcitClcScList.getString("clc_ed_dt")); //#계산종료일자
			ta__tPcitClcScList.setLong("dcn", ta_PcitClcScList.getLong("dcn")); //#일수
			ta__tPcitClcScList.setBigDecimal("amt",
					ta_PcitClcScList.getBigDecimal("amt")); //#금액
			ta__tPcitClcScList.setLong("insl_tim",
					ta_PcitClcScList.getLong("insl_tim")); //#할부회차
			ta__tPcitClcScList.setLong("dwo_seq",
					ta_PcitClcScList.getLong("dwo_seq")); //#인출일련번호
			ta__tPcitClcScList.setString("pcp_rpy_kd_cd",
					ta_PcitClcScList.getString("pcp_rpy_kd_cd")); //#원금상환종류코드
			ta__tPcitClcScList.setLong("clc_ncn",
					ta_PcitClcScList.getLong("clc_ncn")); //#계산건수
			tPcitClcScList.addLData(ta__tPcitClcScList);
		}

		long lClcNoOdr = 1; //#l계산순번

		//yyyyMMddhh24missff4

		//LLog.debug.println("getDateStr : " + KDateUtil.getDateStr());
		//LLog.debug.println("getTimeStr : " + KDateUtil.getTimeStr());
		//LLog.debug.println("getCurrentMileSecond : " + KDateUtil.getCurrentMileSecond());
		//KDateUtil.getCurrentMileSecond()
		//[r여신원리금계산임시등록].[여신원리금처리일련번호] =  KTypeConverter.toString(KDateUtil.getMilliSecond()) ;
		//[r여신원리금계산임시등록].[상담내용] = KDateUtil.getMilliSecond() KDateUtil.getCurrentMileSecond();
		//[r여신원리금계산임시등록].[상담내용] = KDateUtil.getdate("yyyyMMdd");
		//[r여신원리금계산임시등록].[상담내용] = KDateUtil.getTime("HH24MISS");
		//[r여신원리금계산임시등록].[변경사유] = KDateUtil.getTimeStr();
		//[r여신원리금계산임시등록].[변경사유] = KDateUtil.getMilliSecond();
		rDtmGnoInq = lonCnrBasRcvPatEbc.retvDtmGno(); //##일시채번조회

		// =============================================================================
		// ######### GeneralCodeBlock ##s여신원리금처리일련번호 입력
		// =============================================================================
		String sLonPcitPrcSeq = rDtmGnoInq.getString("dtm_gno_txt"); //#s여신원리금처리일련번호
		for (int inx = 0, inxLoopSize = tPcitClcScList.getDataCount(); inx < inxLoopSize; inx++) {
			LData tiPcitClcSc = tPcitClcScList.getLData(inx);
			LProtocolInitializeUtil.primitiveLMultiInitialize(tiPcitClcSc);

			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신원리금계산임시등록  입력값 세팅
				// =============================================================================
				// [i여신원리금계산임시등록]

				iLonPcitClcHisTempReg.setString("lon_pcit_prc_seq",
						sLonPcitPrcSeq);
				iLonPcitClcHisTempReg.setLong("lon_pcit_clc_seq", lClcNoOdr);

				// iLonPcitClcHisTempReg <- tiPcitClcSc;
				iLonPcitClcHisTempReg.setLong("ln_seq",
						tiPcitClcSc.getLong("ln_seq")); //#대출일련번호
				iLonPcitClcHisTempReg.setString("pcit_rcv_clc_dv_cd",
						tiPcitClcSc.getString("pcit_rcv_clc_dv_cd")); //#원리금수납계산구분코드
				iLonPcitClcHisTempReg.setBigDecimal("rckg_amt",
						tiPcitClcSc.getBigDecimal("rckg_amt")); //#기산금액
				iLonPcitClcHisTempReg.setBigDecimal("ap_itt",
						tiPcitClcSc.getBigDecimal("ap_itt")); //#적용금리
				iLonPcitClcHisTempReg.setLong("clc_ncn",
						tiPcitClcSc.getLong("clc_ncn")); //#계산건수
				iLonPcitClcHisTempReg.setLong("insl_tim",
						tiPcitClcSc.getLong("insl_tim")); //#할부회차
				iLonPcitClcHisTempReg.setLong("dwo_seq",
						tiPcitClcSc.getLong("dwo_seq")); //#인출일련번호
				iLonPcitClcHisTempReg.setString("pcp_rpy_kd_cd",
						tiPcitClcSc.getString("pcp_rpy_kd_cd")); //#원금상환종류코드
				// iLonPcitClcHisTempReg <- (LData)iLonPcitClcTempReg.get("pcitClcBas");
				iLonPcitClcHisTempReg.setString("ln_no",
						(((LData) iLonPcitClcTempReg.get("pcitClcBas"))
								.getString("ln_no"))); //#대출번호
				iLonPcitClcHisTempReg.setLong("ln_seq",
						(((LData) iLonPcitClcTempReg.get("pcitClcBas"))
								.getLong("ln_seq"))); //#대출일련번호
				iLonPcitClcHisTempReg.setString("rei_dt",
						(((LData) iLonPcitClcTempReg.get("pcitClcBas"))
								.getString("rei_dt"))); //#영수일자
				iLonPcitClcHisTempReg.setLong("clc_dcn",
						tiPcitClcSc.getLong("dcn"));
				iLonPcitClcHisTempReg.setBigDecimal("clc_amt",
						tiPcitClcSc.getBigDecimal("amt"));
				iLonPcitClcHisTempReg.setString("irt_st_dt",
						tiPcitClcSc.getString("clc_st_dt"));
				iLonPcitClcHisTempReg.setString("irt_ed_dt",
						tiPcitClcSc.getString("clc_ed_dt"));

				lClcNoOdr++;
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonPcitClcHisTempReg);
				rLonPcitClcHisTempReg = lonPcitClcTempEbc
						.regtLonPcitClcHisTemp(iLonPcitClcHisTempReg); //##여신원리금계산내역임시등록
			} catch (LBizDuplicateException e) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 기등록건
				// =============================================================================
				{

					throw new BizException("MZZZ00131", new String[] { "" }, e);
				}
			} catch (LBizException e) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 처리중 에러
				// =============================================================================
				{

					throw new BizException("MZZZ00154", new String[] { "" }, e);
				}
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값전달
		// =============================================================================
		rLonPcitClcTempReg.setString("lon_pcit_prc_seq", sLonPcitPrcSeq);
		LProtocolInitializeUtil.primitiveLMultiInitialize(rLonPcitClcTempReg);
		return rLonPcitClcTempReg;
	}

	/**
	 * 여신즉시이체결과조회
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055116
	 * @logicalName   여신즉시이체결과조회
	 * @param LData iLonImdtTrsRslInq i여신즉시이체결과조회
	 * @return LData rLonImdtTrsRslInq r여신즉시이체결과조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_여신즉시이체결과조회
	 * 
	 */
	public LData retvLonImdtTrsRsl(LData iLonImdtTrsRslInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rLonImdtTrsRslInq = new LData(); //# r여신즉시이체결과조회
		rLonImdtTrsRslInq.set("lonImdtTrsHisDtotb", new LData()); //# r여신즉시이체결과조회.여신즉시이체내역Dtotb
		rLonImdtTrsRslInq.set("lonImdtTrsRslInqRslBas", new LData()); //# r여신즉시이체결과조회.여신즉시이체결과조회결과기본
																		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iLonImdtTrsHisInq = new LData(); //# i여신즉시이체내역조회
		LData rLonImdtTrsHisInq = new LData(); //# r여신즉시이체내역조회
		LData iImdtTrsRslInq = new LData(); //# i즉시이체결과조회
		LData rImdtTrsRslInq = new LData(); //# r즉시이체결과조회
		//#호출 컴포넌트 초기화
		LonImdtTrsHisEbc lonImdtTrsHisEbc = new LonImdtTrsHisEbc(); //# 여신즉시이체내역Ebi
		ImdtTrsIbc imdtTrsIbc = new ImdtTrsIbc(); //# 즉시이체Ibi

		// =============================================================================
		// ######### CodeValidationBlock ##입력 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			//대출번호
			if (KStringUtil.isEmpty(iLonImdtTrsRslInq
					.getString("lon_imdt_trs_id"))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "여신즉시이체ID" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}
		for (int i = 0; i < 8; i++) {

			// =============================================================================
			// ######### GeneralCodeBlock ##sleep 0.5초
			// =============================================================================
			KThreadMngUtil.sleep(500);

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ 0.5초간 정지");
			}
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신즉시이체내역조회  입력값 세팅
				// =============================================================================
				iLonImdtTrsHisInq.setString("lon_imdt_trs_id",
						iLonImdtTrsRslInq.getString("lon_imdt_trs_id"));

				//if (LLog.debug.isEnabled()) {
				//	LLog.debug.println("■■■ ACSD_여신즉시이체결과조회 - 여신즉시이체내역조회  입력값 세팅");
				//	LLog.debug.println([i여신즉시이체내역조회]);
				//}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonImdtTrsHisInq);
				rLonImdtTrsHisInq = lonImdtTrsHisEbc
						.retvLonImdtTrsHis(iLonImdtTrsHisInq); //##여신즉시이체내역조회

				// =============================================================================
				// ######### GeneralCodeBlock ##즉시이체내역 결과로그
				// =============================================================================
				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■ ACSD_여신즉시이체결과조회 - 즉시이체내역 결과로그");
					LLog.debug.println(rLonImdtTrsHisInq);
				}
				if (KLDataConvertUtil.notEquals(
						rLonImdtTrsHisInq.getString("trs_ss_cd"), "01")) {

					// =============================================================================
					// ######### GeneralCodeBlock ##break
					// =============================================================================
					break;
				}
			} catch (LBizNotFoundException e) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##이체ID를 확인하십시오.
				// =============================================================================
				{

					throw new BizException("MZZZ00083",
							new String[] { "이체ID" }, e);
				}
			} catch (LBizException e) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##즉시이체내역 조회 중 오류가 발생하였습니다.
				// =============================================================================
				{

					throw new BizException("MBCM00193",
							new String[] { "즉시이체내역" }, e);
				}
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##즉시이체결과조회  입력값 세팅
		// =============================================================================
		iImdtTrsRslInq.setString("imdt_trs_dln_id",
				rLonImdtTrsHisInq.getString("imdt_trs_dln_id"));

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_즉시이체수납결과조회 - 즉시이체결과조회  입력값 세팅");
			LLog.debug.println(iImdtTrsRslInq);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(iImdtTrsRslInq);
		rImdtTrsRslInq = imdtTrsIbc.retvImdtTrsRsl(iImdtTrsRslInq); //##즉시이체결과조회

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 입력
		// =============================================================================
		rLonImdtTrsRslInq.set("lonImdtTrsHisDtotb", new LData());
		rLonImdtTrsRslInq.set("lonImdtTrsRslInqRslBas", new LData());

		LProtocolInitializeUtil
				.primitiveLMultiInitialize((LData) rLonImdtTrsRslInq
						.get("lonImdtTrsHisDtotb"));
		LProtocolInitializeUtil
				.primitiveLMultiInitialize((LData) rLonImdtTrsRslInq
						.get("lonImdtTrsRslInqRslBas"));

		//동일 DTO
		// (LData)rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")<- rLonImdtTrsHisInq;
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"lon_imdt_trs_id",
				rLonImdtTrsHisInq.getString("lon_imdt_trs_id")); //#여신즉시이체ID
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"imdt_trs_dln_id",
				rLonImdtTrsHisInq.getString("imdt_trs_dln_id")); //#즉시이체거래ID
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"tsm_dt", rLonImdtTrsHisInq.getString("tsm_dt")); //#전송일자
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"tsm_tm", rLonImdtTrsHisInq.getString("tsm_tm")); //#전송시각
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"trs_dv_cd", rLonImdtTrsHisInq.getString("trs_dv_cd")); //#이체구분코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"trs_dty_cd", rLonImdtTrsHisInq.getString("trs_dty_cd")); //#이체업무코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"trs_chn_dv_cd", rLonImdtTrsHisInq.getString("trs_chn_dv_cd")); //#이체채널구분코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"ig_cs_no", rLonImdtTrsHisInq.getString("ig_cs_no")); //#통합고객번호
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"lon_pdt_cd", rLonImdtTrsHisInq.getString("lon_pdt_cd")); //#여신상품코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb"))
				.setString("lon_pdt_sce_no",
						rLonImdtTrsHisInq.getString("lon_pdt_sce_no")); //#여신상품SCOPE번호
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"ln_no", rLonImdtTrsHisInq.getString("ln_no")); //#대출번호
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setLong("ln_seq",
				rLonImdtTrsHisInq.getLong("ln_seq")); //#대출일련번호
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setLong(
				"dwo_seq", rLonImdtTrsHisInq.getLong("dwo_seq")); //#인출일련번호
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"daw_mtacc_id", rLonImdtTrsHisInq.getString("daw_mtacc_id")); //#입출금모계좌ID
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"bnk_cd", rLonImdtTrsHisInq.getString("bnk_cd")); //#은행코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"fin_aco_id", rLonImdtTrsHisInq.getString("fin_aco_id")); //#금융계좌ID
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setBigDecimal(
				"dln_amt", rLonImdtTrsHisInq.getBigDecimal("dln_amt")); //#거래금액
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"tg_dv_cd", rLonImdtTrsHisInq.getString("tg_dv_cd")); //#전문구분코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"trs_ss_cd", rLonImdtTrsHisInq.getString("trs_ss_cd")); //#이체상태코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setBigDecimal(
				"dln_fee", rLonImdtTrsHisInq.getBigDecimal("dln_fee")); //#거래수수료
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"temp_ctpl_txt", rLonImdtTrsHisInq.getString("temp_ctpl_txt")); //#임시연락처내용
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"cnl_yn", rLonImdtTrsHisInq.getString("cnl_yn")); //#취소여부
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"rcv_his_eyn", rLonImdtTrsHisInq.getString("rcv_his_eyn")); //#수납내역유무
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"trs_rcv_dv_cd", rLonImdtTrsHisInq.getString("trs_rcv_dv_cd")); //#이체수납구분코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"rfln_dt", rLonImdtTrsHisInq.getString("rfln_dt")); //#반영일자
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"rfln_tm", rLonImdtTrsHisInq.getString("rfln_tm")); //#반영시각
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"rmk_txt", rLonImdtTrsHisInq.getString("rmk_txt")); //#비고내용
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"prc_orz_cd", rLonImdtTrsHisInq.getString("prc_orz_cd")); //#처리조직코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"acny_prc_orz_cd",
				rLonImdtTrsHisInq.getString("acny_prc_orz_cd")); //#경리처리조직코드
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"prc_ip_adr", rLonImdtTrsHisInq.getString("prc_ip_adr")); //#처리IP주소
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"prc_prog_id", rLonImdtTrsHisInq.getString("prc_prog_id")); //#처리프로그램ID
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"fs_in_usr_id", rLonImdtTrsHisInq.getString("fs_in_usr_id")); //#최초입력사용자ID
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"fs_in_dtm", rLonImdtTrsHisInq.getString("fs_in_dtm")); //#최초입력일시
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"ls_ch_usr_id", rLonImdtTrsHisInq.getString("ls_ch_usr_id")); //#최종변경사용자ID
		((LData) rLonImdtTrsRslInq.get("lonImdtTrsHisDtotb")).setString(
				"ls_ch_dtm", rLonImdtTrsHisInq.getString("ls_ch_dtm")); //#최종변경일시

		((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas")).setString(
				"imdt_trs_etn_tg_resp_cd",
				rImdtTrsRslInq.getString("imdt_trs_etn_tg_resp_cd")); //#즉시이체대외전문응답코드

		if (KLDataConvertUtil.equals(rLonImdtTrsHisInq.getString("trs_ss_cd"),
				"01")) { //이체중
			((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas"))
					.setString("rcv_prc_rsl_cd", "");
			((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas"))
					.setString("rcv_prc_rsl_nm", "무응답");

		} else if (KLDataConvertUtil.equals(
				rLonImdtTrsHisInq.getString("trs_ss_cd"), "02")) { //정상
			((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas"))
					.setString("rcv_prc_rsl_cd", "0");
			((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas"))
					.setString("rcv_prc_rsl_nm", "정상");

		} else if (KLDataConvertUtil.equals(
				rLonImdtTrsHisInq.getString("trs_ss_cd"), "06")) { //계산오류
			((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas"))
					.setString("rcv_prc_rsl_cd", "1");
			((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas"))
					.setString("rcv_prc_rsl_nm", "계산오류");

		} else {
			((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas"))
					.setString("rcv_prc_rsl_cd", "9");
			((LData) rLonImdtTrsRslInq.get("lonImdtTrsRslInqRslBas"))
					.setString("rcv_prc_rsl_nm", "은행오류");

		}

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_여신즉시이체결과조회 - 결과값 입력");
			LLog.debug.println(rLonImdtTrsRslInq);
		}
		LProtocolInitializeUtil.LdataInitialize(rLonImdtTrsRslInq);
		return rLonImdtTrsRslInq;
	}

	/**
	 * 연체기산일산출
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055105
	 * @logicalName   연체기산일산출
	 * @param LData iAaRckgDayCu i연체기산일산출
	 * @return LData rAaRckgDayCu r연체기산일산출
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_연체기산일산출
	 * 
	 */
	public LData cmptAaRckgDay(LData iAaRckgDayCu) throws LException {
		//#Return 변수 선언 및 초기화
		LData rAaRckgDayCu = new LData(); //# r연체기산일산출
											//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iLonCnrBasInq = new LData(); //# i여신계약기본조회
		LData rLonCnrBasInq = new LData(); //# r여신계약기본조회
		LData iAffcAaBasInq = new LData(); //# i사후연체기본조회
		LData rAffcAaBasInq = new LData(); //# r사후연체기본조회
		LData iRttOrzOlnDetsInq = new LData(); //# i퇴직조직원대출명세조회
		LData rRttOrzOlnDetsInq = new LData(); //# r퇴직조직원대출명세조회
		LData iLonHseCapMgtHisLnNoListInq = new LData(); //# i여신주택자금관리내역대출번호목록조회
		LMultiData rLonHseCapMgtHisLnNoListInq = new LMultiData(); //# r여신주택자금관리내역대출번호목록조회
		LData iPrsdWrkOmHisIgCsNoListInq = new LData(); //# i주재근무조직원내역통합고객번호목록조회
		LMultiData rPrsdWrkOmHisIgCsNoListInq = new LMultiData(); //# r주재근무조직원내역통합고객번호목록조회
		LData iIdlRetiAcoSptEmpMgtBsInq = new LData(); //# i개인은퇴계좌지원사원관리기준조회
		LData rIdlRetiAcoSptEmpMgtBsInq = new LData(); //# r개인은퇴계좌지원사원관리기준조회
		LData iEsopTgtLdgHisOmNoInq = new LData(); //# i우리사주대상원장내역조직원번호조회
		LMultiData rEsopTgtLdgHisOmNoInq = new LMultiData(); //# r우리사주대상원장내역조직원번호조회
		LData iHseScySelCtrlTlmPrfFrftDtRcpNoInq = new LData(); //# i주택담보매각통제기한이익상실일자접수번호조회
		LData rHseScySelCtrlTlmPrfFrftDtRcpNoInq = new LData(); //# r주택담보매각통제기한이익상실일자접수번호조회
		LData iAaNotiHsCabyInq = new LData(); //# i연체통지이력건별조회
		LData rAaNotiHsCabyInq = new LData(); //# r연체통지이력건별조회
		LData iDebMetPrsSsInq = new LData(); //# i채무조정진행상태조회
		LData rDebMetPrsSsInq = new LData(); //# r채무조정진행상태조회
		LData iLonRcpBasPspnCondChListInq = new LData(); //# i여신접수기본연기조건변경목록조회
		LMultiData rLonRcpBasPspnCondChListInq = new LMultiData(); //# r여신접수기본연기조건변경목록조회
		//#호출 컴포넌트 초기화
		LonCnrBasEbc lonCnrBasEbc = new LonCnrBasEbc(); //# 여신계약기본Ebi
		AffcAaBasEbc affcAaBasEbc = new AffcAaBasEbc(); //# 사후연체기본Ebi
		RttOrzOlnDetsEbc rttOrzOlnDetsEbc = new RttOrzOlnDetsEbc(); //# 퇴직조직원대출명세Ebi
		LonHseCapMgtHisEbc lonHseCapMgtHisEbc = new LonHseCapMgtHisEbc(); //# 여신주택자금관리내역Ebi
		PrsdWrkOmHisEbc prsdWrkOmHisEbc = new PrsdWrkOmHisEbc(); //# 주재근무조직원내역Ebi
		IdlRetiAcoSptEmpMgtBsEbc idlRetiAcoSptEmpMgtBsEbc = new IdlRetiAcoSptEmpMgtBsEbc(); //# 개인은퇴계좌지원사원관리기준Ebi
		EsopTgtLdgHisEbc esopTgtLdgHisEbc = new EsopTgtLdgHisEbc(); //# 우리사주대상원장내역Ebi
		HseScySelCtrlHisEbc hseScySelCtrlHisEbc = new HseScySelCtrlHisEbc(); //# 주택담보매각통제내역Ebi
		LonCnrBasRcvPatEbc lonCnrBasRcvPatEbc = new LonCnrBasRcvPatEbc(); //# 여신계약기본수납파트Ebi
		DebMetPrsHisEbc debMetPrsHisEbc = new DebMetPrsHisEbc(); //# 채무조정진행내역Ebi
		LonRcpBasEbc lonRcpBasEbc = new LonRcpBasEbc(); //# 여신접수기본Ebi

		// =============================================================================
		// ######### CodeValidationBlock ##입력 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_연체기산일산출 - 입력 검증");
				LLog.debug.println(iAaRckgDayCu);
			}

			if (KLDataConvertUtil.equals(iAaRckgDayCu.getString("in_dv"), "1")) {
				if (KStringUtil.isEmpty(iAaRckgDayCu.getString("ln_no"))) {
					bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
							new String[] { "대출번호" });
					existInvalidElement = true;
				}
				if (KStringUtil.isEmpty(iAaRckgDayCu.getLong("ln_seq"))) {
					bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
							new String[] { "대출일련번호" });
					existInvalidElement = true;
				}
			} else if (KLDataConvertUtil.equals(
					iAaRckgDayCu.getString("in_dv"), "2")) {
				if (KStringUtil.isEmpty(iAaRckgDayCu.getString("in_dt"))) {
					bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
							new String[] { "입력일자" });
					existInvalidElement = true;
				}
			} else {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "입력구분" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 설정
		// =============================================================================
		String sRpyDt = "00010101"; //#s상환일자
		String sExpDt = "00010101"; //#s만기일자

		String sRealRpyDt = "00010101"; //#s실제상환일자
		String sAaStDt = "00010101"; //#s연체시작일자

		int iMnCnt = 0; //#i개월수
		String sTlmPrfFrftDt = "00010101"; //#s기한이익상실일자
		String sPspnCnsDt = "00010101"; //#s연기승인일자
		String sLonCsCfDt = "00010101"; //#s여신고객확인일자 //통지도달일 + 10영업일
		String sSndDay = "00010101"; //#s발송일 // 통지발송일
		String sDebMetEdDt = "00010101"; //#s채무조정종료일자 //채무조정 업무절차 종료일자

		String sAaYn = "N"; //#s연체여부

		String sSndYn = "N"; //#s발송여부
		String sDebMetTgtYn = "N"; //#s채무조정대상여부

		String sNowDt = KDateUtil.getCurrentDate("yyyyMMdd"); //#s현재일자

		// =============================================================================
		// ######### GeneralCodeBlock ##결과 초기값 입력
		// =============================================================================
		//초기값 SET
		rAaRckgDayCu.setString("tlm_prf_frft_dt", "00010101");
		rAaRckgDayCu.setString("aa_st_dt", "00010101");
		rAaRckgDayCu.setString("real_rpy_dt", "00010101");
		if (KLDataConvertUtil.equals(iAaRckgDayCu.getString("in_dv"), "1") //대출번호기준
		) {
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신계약기본조회  입력값 세팅
				// =============================================================================
				iLonCnrBasInq.setString("ln_no",
						iAaRckgDayCu.getString("ln_no"));
				iLonCnrBasInq.setLong("ln_seq", iAaRckgDayCu.getLong("ln_seq"));

				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■ ACSD_연체기산일산출 - 여신계약기본조회 START");
					LLog.debug.println(iLonCnrBasInq);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonCnrBasInq);
				rLonCnrBasInq = lonCnrBasEbc.retvLonCnrBas(iLonCnrBasInq); //##여신계약기본조회
			} catch (LBizNotFoundException e) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 계약사항 조회 중 오류가 발생하였습니다.
				// =============================================================================
				{

					//계약사항 조회 중 오류가 발생하였습니다.
					throw new BizException("MBCM00193",
							new String[] { "계약사항" }, e);
				}
			}
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##사후연체기본조회  입력값 세팅
				// =============================================================================
				// [i사후연체기본조회]

				iAffcAaBasInq = new LData();
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iAffcAaBasInq);

				iAffcAaBasInq.setString("ln_no",
						rLonCnrBasInq.getString("ln_no"));
				iAffcAaBasInq
						.setLong("ln_seq", rLonCnrBasInq.getLong("ln_seq"));
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iAffcAaBasInq);
				rAffcAaBasInq = affcAaBasEbc.retvAffcAaBas(iAffcAaBasInq); //##사후연체기본조회

				// =============================================================================
				// ######### GeneralCodeBlock ##사후연체기본조회  결과값 맵핑
				// =============================================================================
				// [r사후연체기본조회]

				sAaYn = rAffcAaBasInq.getString("aa_yn");

				rAaRckgDayCu.setString("aa_yn",
						rAffcAaBasInq.getString("aa_yn"));
			} catch (LBizNotFoundException e) {

				// =============================================================================
				// ######### GeneralCodeBlock ##연체여부세팅
				// =============================================================================
				rAaRckgDayCu.setString("aa_yn", "N");
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##s상환일자 세팅
			// =============================================================================
			if ((rLonCnrBasInq.getString("nt_rpy_dt")).compareTo("00010101") > 0) {

				if ((rLonCnrBasInq.getString("nt_rpy_dt"))
						.compareTo(rLonCnrBasInq.getString("pa_fsr_dt")) < 0) {
					sRpyDt = rLonCnrBasInq.getString("nt_rpy_dt");
				} else {
					sRpyDt = rLonCnrBasInq.getString("pa_fsr_dt");
				}
			} else {
				sRpyDt = rLonCnrBasInq.getString("pa_fsr_dt");
			}

			//20250512 수정 - 특수채권 연체기산일을 일반채권과 동일하게
			// 대손상각시 응당일이 정상화되므로 상각이전 상태로 유지
			// [연체발생기준일자] => 대출원장 납입응당일OR만기일 이고 [연체발생일]은 연체계산시작일임
			if (KLDataConvertUtil.equals(
					rLonCnrBasInq.getString("spc_bd_reg_yn"), "Y")) {

				if ((rAffcAaBasInq.getString("aa_occ_bs_dt")).compareTo(sRpyDt) < 0) {

					sRpyDt = rAffcAaBasInq.getString("aa_occ_bs_dt");

				}

			}

			//기한이익 상실일자 체크(퇴직, IRA해지 등)
			{
				if (KLDataConvertUtil.equals(
						rLonCnrBasInq.getString("lon_pdt_cd"), "106003") //보증보험담보(임사원생활안정)
						|| KLDataConvertUtil
								.equals(rLonCnrBasInq.getString("lon_pdt_cd"),
										"106004") //보증보험담보(생활설계사)
						|| KLDataConvertUtil
								.equals(rLonCnrBasInq.getString("lon_pdt_cd"),
										"107001") //사원임차자금대출
						|| KLDataConvertUtil
								.equals(rLonCnrBasInq.getString("lon_pdt_cd"),
										"107002") //사원주택자금대출
						|| (KLDataConvertUtil
								.equals(rLonCnrBasInq.getString("lon_pdt_cd"),
										"104020") && KLDataConvertUtil
								.equals(rLonCnrBasInq
										.getString("lon_pdt_sce_no"), "01")) //IRA가입대상자(교보생명IRA)
						|| KLDataConvertUtil
								.equals(rLonCnrBasInq.getString("lon_pdt_cd"),
										"104021") //우리사주가입대상자
				) {
					try {

						// =============================================================================
						// ######### GeneralCodeBlock ##퇴직조직원대출명세조회  입력값 세팅
						// =============================================================================
						iRttOrzOlnDetsInq.setString("ln_no",
								iAaRckgDayCu.getString("ln_no"));
						iRttOrzOlnDetsInq.setLong("ln_seq", 0);

						if (LLog.debug.isEnabled()) {
							LLog.debug
									.println("■■■ ACSD_연체기산일산출 - 퇴직조직원대출명세조회 START");
							LLog.debug.println(iRttOrzOlnDetsInq);
						}
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iRttOrzOlnDetsInq);
						rRttOrzOlnDetsInq = rttOrzOlnDetsEbc
								.retvRttOrzOlnDets(iRttOrzOlnDetsInq); //##퇴직조직원대출명세조회

						// =============================================================================
						// ######### GeneralCodeBlock ##퇴직조직원대출명세조회  결과값 맵핑
						// =============================================================================
						// [r퇴직조직원대출명세조회]
						if (KLDataConvertUtil
								.equals(rLonCnrBasInq.getString("lon_pdt_cd"),
										"106004")
								&& (rRttOrzOlnDetsInq.getString("rtt_dt"))
										.compareTo("00010101") > 0) {
							// -1일

							if ((rLonCnrBasInq.getString("exp_dt"))
									.compareTo(rRttOrzOlnDetsInq
											.getString("rtt_dt")) > 0) {
								rLonCnrBasInq.setString("exp_dt", KDateUtil
										.addDay(rRttOrzOlnDetsInq
												.getString("rtt_dt"), -1));
							}
						} else {
							if ((rRttOrzOlnDetsInq.getString("rtpy_pn_dt"))
									.compareTo("00010101") > 0) {
								if ((rLonCnrBasInq.getString("exp_dt"))
										.compareTo(rRttOrzOlnDetsInq
												.getString("rtpy_pn_dt")) > 0) {
									rLonCnrBasInq.setString("exp_dt",
											rRttOrzOlnDetsInq
													.getString("rtpy_pn_dt"));
								}
							}
						}
					} catch (LBizNotFoundException e) {
					}
					// 사원주택자금대출시 기한이익상실 처리
					if (KLDataConvertUtil.equals(
							rLonCnrBasInq.getString("lon_pdt_cd"), "107001")
							|| KLDataConvertUtil.equals(
									rLonCnrBasInq.getString("lon_pdt_cd"),
									"107002")) {

						// =============================================================================
						// ######### GeneralCodeBlock ##여신주택자금관리내역[대출번호]목록조회  입력값 세팅
						// =============================================================================
						iLonHseCapMgtHisLnNoListInq.setString("ln_no",
								iAaRckgDayCu.getString("ln_no"));
						iLonHseCapMgtHisLnNoListInq.setLong("ln_seq",
								iAaRckgDayCu.getLong("ln_seq"));
						iLonHseCapMgtHisLnNoListInq.setString(
								"tlm_prf_frft_dt", "00010101");

						if (LLog.debug.isEnabled()) {
							LLog.debug
									.println("■■■ ACSD_연체기산일산출 - 여신주택자금관리내역lnNo목록조회  입력값 세팅");
							LLog.debug.println(iLonHseCapMgtHisLnNoListInq);
						}
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iLonHseCapMgtHisLnNoListInq);
						rLonHseCapMgtHisLnNoListInq = lonHseCapMgtHisEbc
								.retvLstLonHseCapMgtHisByLnNo(iLonHseCapMgtHisLnNoListInq); //##여신주택자금관리내역[대출번호]목록조회
						for (int inx = 0, inxLoopSize = rLonHseCapMgtHisLnNoListInq
								.getDataCount(); inx < inxLoopSize; inx++) {
							LData tLonHseCapMgtHis = rLonHseCapMgtHisLnNoListInq
									.getLData(inx);
							LProtocolInitializeUtil
									.primitiveLMultiInitialize(tLonHseCapMgtHis);

							// =============================================================================
							// ######### GeneralCodeBlock ##여신주택자금관리내역[대출번호]목록조회  결과값 맵핑
							// =============================================================================
							if ((rRttOrzOlnDetsInq.getString("rtpy_pn_dt"))
									.compareTo("00010101") > 0) {
								if ((rLonCnrBasInq.getString("exp_dt"))
										.compareTo(tLonHseCapMgtHis
												.getString("tlm_prf_frft_dt")) > 0) {
									rLonCnrBasInq
											.setString(
													"exp_dt",
													tLonHseCapMgtHis
															.getString("tlm_prf_frft_dt"));
								}
							} else {
								rLonCnrBasInq.setString("exp_dt",
										tLonHseCapMgtHis
												.getString("tlm_prf_frft_dt"));
							}

							// =============================================================================
							// ######### GeneralCodeBlock ##break
							// =============================================================================
							break;
						}
					}
					// IRA 해지 체크
					if (KLDataConvertUtil.equals(
							rLonCnrBasInq.getString("lon_pdt_cd"), "104020")) {

						// =============================================================================
						// ######### GeneralCodeBlock ##주재근무조직원내역[통합고객번호]목록조회  입력값 세팅
						// =============================================================================
						iPrsdWrkOmHisIgCsNoListInq.setString("ig_cs_no",
								rLonCnrBasInq.getString("ig_cs_no"));
						iPrsdWrkOmHisIgCsNoListInq.setString("hmre_tp_cd",
								"117"); //내근직원

						if (LLog.debug.isEnabled()) {
							LLog.debug
									.println("■■■ ACSD_연체기산일산출 - 주재근무조직원내역igCsNo목록조회 START");
							LLog.debug.println(iPrsdWrkOmHisIgCsNoListInq);
						}
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iPrsdWrkOmHisIgCsNoListInq);
						rPrsdWrkOmHisIgCsNoListInq = prsdWrkOmHisEbc
								.retvLstPrsdWrkOmHisByIgCsNo(iPrsdWrkOmHisIgCsNoListInq); //##주재근무조직원내역[통합고객번호]목록조회
						for (int jnx = 0, jnxLoopSize = rPrsdWrkOmHisIgCsNoListInq
								.getDataCount(); jnx < jnxLoopSize; jnx++) {
							LData tPrsdWrkOmHis = rPrsdWrkOmHisIgCsNoListInq
									.getLData(jnx);
							LProtocolInitializeUtil
									.primitiveLMultiInitialize(tPrsdWrkOmHis);

							try {

								// =============================================================================
								// ######### GeneralCodeBlock ##개인은퇴계좌지원사원관리기준조회  입력값 세팅
								// =============================================================================
								iIdlRetiAcoSptEmpMgtBsInq.setString("om_no",
										tPrsdWrkOmHis
												.getString("prsd_wrk_om_no"));
								LProtocolInitializeUtil
										.primitiveLMultiInitialize(iIdlRetiAcoSptEmpMgtBsInq);
								rIdlRetiAcoSptEmpMgtBsInq = idlRetiAcoSptEmpMgtBsEbc
										.retvIdlRetiAcoSptEmpMgtBs(iIdlRetiAcoSptEmpMgtBsInq); //##개인은퇴계좌지원사원관리기준조회

								// =============================================================================
								// ######### GeneralCodeBlock ##만기일자 - 가입금액수령일자 변경
								// =============================================================================
								if ((rIdlRetiAcoSptEmpMgtBsInq
										.getString("sbc_amt_recp_dt"))
										.compareTo("00010101") > 0
										&& KLDataConvertUtil.equals(
												rIdlRetiAcoSptEmpMgtBsInq
														.getString("cnl_yn"),
												"N")) {
									if ((rLonCnrBasInq.getString("exp_dt"))
											.compareTo(rIdlRetiAcoSptEmpMgtBsInq
													.getString("sbc_amt_recp_dt")) > 0) {
										rLonCnrBasInq
												.setString(
														"exp_dt",
														rIdlRetiAcoSptEmpMgtBsInq
																.getString("sbc_amt_recp_dt"));
									}
								}
							} catch (LBizNotFoundException e) {
							}
						}
					}
					// 우리사주해지 체크
					if (KLDataConvertUtil.equals(
							rLonCnrBasInq.getString("lon_pdt_cd"), "104021")) {

						// =============================================================================
						// ######### GeneralCodeBlock ##주재근무조직원내역[통합고객번호]목록조회  입력값 세팅
						// =============================================================================
						iPrsdWrkOmHisIgCsNoListInq.setString("ig_cs_no",
								rLonCnrBasInq.getString("ig_cs_no"));
						iPrsdWrkOmHisIgCsNoListInq.setString("hmre_tp_cd",
								"117"); //내근직원

						LLog.debug
								.println("■■■ ACSD_연체기산일산출 - 주재근무조직원내역igCsNo목록조회 START");
						LLog.debug.println(iPrsdWrkOmHisIgCsNoListInq);
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iPrsdWrkOmHisIgCsNoListInq);
						rPrsdWrkOmHisIgCsNoListInq = prsdWrkOmHisEbc
								.retvLstPrsdWrkOmHisByIgCsNo(iPrsdWrkOmHisIgCsNoListInq); //##주재근무조직원내역[통합고객번호]목록조회
						for (int knx = 0, knxLoopSize = rPrsdWrkOmHisIgCsNoListInq
								.getDataCount(); knx < knxLoopSize; knx++) {
							LData tPrsdWrkOmHis = rPrsdWrkOmHisIgCsNoListInq
									.getLData(knx);
							LProtocolInitializeUtil
									.primitiveLMultiInitialize(tPrsdWrkOmHis);

							// =============================================================================
							// ######### GeneralCodeBlock ##우리사주대상원장내역[조직원번호]목록조회  입력값 세팅
							// =============================================================================
							iEsopTgtLdgHisOmNoInq.setString("om_no",
									tPrsdWrkOmHis.getString("prsd_wrk_om_no"));
							LProtocolInitializeUtil
									.primitiveLMultiInitialize(iEsopTgtLdgHisOmNoInq);
							rEsopTgtLdgHisOmNoInq = esopTgtLdgHisEbc
									.retvLstEsopTgtLdgHisByOmNo(iEsopTgtLdgHisOmNoInq); //##우리사주대상원장내역[조직원번호]목록조회
							for (int lnx = 0, lnxLoopSize = rEsopTgtLdgHisOmNoInq
									.getDataCount(); lnx < lnxLoopSize; lnx++) {
								LData tEsopTgtLdgHis = rEsopTgtLdgHisOmNoInq
										.getLData(lnx);
								LProtocolInitializeUtil
										.primitiveLMultiInitialize(tEsopTgtLdgHis);

								// =============================================================================
								// ######### GeneralCodeBlock ##만기일자 - 우리사주해지일자 변경
								// =============================================================================
								if ((rLonCnrBasInq.getString("exp_dt"))
										.compareTo(tEsopTgtLdgHis
												.getString("esop_trm_dt")) > 0
										&& (tEsopTgtLdgHis
												.getString("esop_trm_dt"))
												.compareTo("00010101") > 0) {
									rLonCnrBasInq.setString("exp_dt",
											tEsopTgtLdgHis
													.getString("esop_trm_dt"));
								}

								// =============================================================================
								// ######### GeneralCodeBlock ##break
								// =============================================================================
								break;
							}
						}
					}
				}
				if (KLDataConvertUtil.equals(
						rLonCnrBasInq.getString("lon_pdt_cd"), "104020")
						&& KLDataConvertUtil
								.equals(rLonCnrBasInq
										.getString("lon_pdt_sce_no"), "02")) {
					// IRA가입대상자(교보문고IRA)
					try {

						// =============================================================================
						// ######### GeneralCodeBlock ##개인은퇴계좌지원사원관리기준조회  입력값 세팅
						// =============================================================================
						iIdlRetiAcoSptEmpMgtBsInq.setString("om_no",
								rLonCnrBasInq.getString("ig_cs_no"));
						if (LLog.debug.isEnabled()) {
							LLog.debug
									.println("■■■ ACSD_연체기산일산출 - 개인은퇴계좌지원사원관리기준조회 START2");
							LLog.debug.println(iIdlRetiAcoSptEmpMgtBsInq);
						}
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iIdlRetiAcoSptEmpMgtBsInq);
						rIdlRetiAcoSptEmpMgtBsInq = idlRetiAcoSptEmpMgtBsEbc
								.retvIdlRetiAcoSptEmpMgtBs(iIdlRetiAcoSptEmpMgtBsInq); //##개인은퇴계좌지원사원관리기준조회

						// =============================================================================
						// ######### GeneralCodeBlock ##만기일자 - 가입금액수령일자 변경
						// =============================================================================
						if ((rIdlRetiAcoSptEmpMgtBsInq
								.getString("sbc_amt_recp_dt"))
								.compareTo("00010101") > 0
								&& KLDataConvertUtil.equals(
										rIdlRetiAcoSptEmpMgtBsInq
												.getString("cnl_yn"), "N")) {
							if ((rLonCnrBasInq.getString("exp_dt"))
									.compareTo(rIdlRetiAcoSptEmpMgtBsInq
											.getString("sbc_amt_recp_dt")) > 0) {
								rLonCnrBasInq.setString("exp_dt",
										rIdlRetiAcoSptEmpMgtBsInq
												.getString("sbc_amt_recp_dt"));
								if (LLog.debug.isEnabled()) {
									LLog.debug
											.println("■■■ 개인은퇴계좌지원사원관리기준조회 후 가입금액수령일자로 만기일자 변경");
									LLog.debug.println(rLonCnrBasInq
											.getString("exp_dt"));
								}
							}
						}
					}
					// IRA가입대상자(교보문고IRA)
					catch (LBizNotFoundException e) {
					}
				}
				try {

					// =============================================================================
					// ######### GeneralCodeBlock ##주택담보매각통제기한이익상실일자[접수번호]조회  입력값 세팅
					// =============================================================================
					iHseScySelCtrlTlmPrfFrftDtRcpNoInq.setString("lon_rcp_no",
							rLonCnrBasInq.getString("lon_rcp_no"));
					iHseScySelCtrlTlmPrfFrftDtRcpNoInq.setString("del_yn", "N");

					if (LLog.debug.isEnabled()) {
						LLog.debug
								.println("■■■ ACSD_연체기산일산출 - 주택담보매각통제기한이익상실일자rcpNo조회  입력값 세팅");
						LLog.debug.println(iHseScySelCtrlTlmPrfFrftDtRcpNoInq);
					}
					LProtocolInitializeUtil
							.primitiveLMultiInitialize(iHseScySelCtrlTlmPrfFrftDtRcpNoInq);
					rHseScySelCtrlTlmPrfFrftDtRcpNoInq = hseScySelCtrlHisEbc
							.retvHseScySelCtrlTlmPrfFrftDtByRcpNo(iHseScySelCtrlTlmPrfFrftDtRcpNoInq); //##주택담보매각통제기한이익상실일자[접수번호]조회

					// =============================================================================
					// ######### GeneralCodeBlock ##만기일자 - 기한이익상실일자 변경
					// =============================================================================
					if ((rHseScySelCtrlTlmPrfFrftDtRcpNoInq
							.getString("tlm_prf_frft_dt"))
							.compareTo("00010101") > 0
							&& (rHseScySelCtrlTlmPrfFrftDtRcpNoInq
									.getString("hse_sel_dt2"))
									.compareTo("00010101") <= 0) {
						if ((rHseScySelCtrlTlmPrfFrftDtRcpNoInq
								.getString("tlm_prf_frft_dt"))
								.compareTo("00010101") > 0) {
							if ((rLonCnrBasInq.getString("exp_dt"))
									.compareTo(rHseScySelCtrlTlmPrfFrftDtRcpNoInq
											.getString("tlm_prf_frft_dt")) > 0) {
								rLonCnrBasInq.setString("exp_dt",
										rHseScySelCtrlTlmPrfFrftDtRcpNoInq
												.getString("tlm_prf_frft_dt"));
								if (LLog.debug.isEnabled()) {
									LLog.debug
											.println("■■■ 주택담보매각통제기한이익상실일자 후 기한이익상실일자로 만기일자 변경");
									LLog.debug.println(rLonCnrBasInq
											.getString("exp_dt"));
								}
							}
						}
					}
				} catch (LBizNotFoundException e) {
				}
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##s실제상환일자, s연체시작일자 산출
			// =============================================================================
			sExpDt = rLonCnrBasInq.getString("exp_dt");

			if (sExpDt.compareTo("00010101") > 0) {
				if (sExpDt.compareTo(sRpyDt) <= 0) {
					sRpyDt = sExpDt;

					//기한이익상실일
					rAaRckgDayCu.setString("tlm_prf_frft_dt", sExpDt);
				}
			}

			rAaRckgDayCu.setString("tlm_prf_frft_dt2",
					rAaRckgDayCu.getString("tlm_prf_frft_dt"));

			rAaRckgDayCu.setString("aa_kd", "2"); //잔액연체

			LLog.debug
					.println("■■■ ACSD_연체기산일산출 - s실제상환일자, s연체시작일자 산출 / s상환일자    :"
							+ sRpyDt);

			sRealRpyDt = KBizDateUtil.getBizDateNextHoliday(sRpyDt);

			LLog.debug
					.println("■■■ ACSD_연체기산일산출 - s실제상환일자, s연체시작일자 산출 / s실제상환일자:"
							+ sRealRpyDt);

			sAaStDt = KDateUtil.addDay(sRealRpyDt, 1);

			LLog.debug
					.println("■■■ ACSD_연체기산일산출 - s실제상환일자, s연체시작일자 산출 / s연체시작일자:"
							+ sAaStDt);
			if ((rAaRckgDayCu.getString("tlm_prf_frft_dt"))
					.compareTo("00010101") <= 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##기한이익상실일자 산출
				// =============================================================================
				String sTlmPrfFrftPstDvCd1 = KStringUtil
						.substring(rLonCnrBasInq
								.getString("tlm_prf_frft_pst_dv_cd"), 0, 1); //#s기한이익상실유예구분코드1

				if (KLDataConvertUtil.equals(sTlmPrfFrftPstDvCd1, "M")) {
					//실납입응당일자가 말일자라면
					if (KDateUtil.isLastDay(sRealRpyDt)) {
						//매각건증 20140401 이후 이자유예 발생건 2개월 유예
						//20180104 수정(주택담보대출 기한이익상실 2개월 유예반영) 
						//주택담보대출이라하더라도 기한이익상실일이 2018-01-12 이전건은 기존 1개월유예 유지 
						//(즉 이자유예연체시작일이  2017-12-13 이후건부터 2개월적용 LCCE01_PLS_GCD => M02 신규코드부여됐으나 무조건 2개월이 아님
						if ((KLDataConvertUtil.equals(
								rLonCnrBasInq.getString("lon_sel_dv_cd"), "01") && sAaStDt
								.compareTo("20140228") >= 0)
								|| (KLDataConvertUtil.equals(rLonCnrBasInq
										.getString("hse_scy_ln_yn"), "Y") && sAaStDt
										.compareTo("20171213") >= 0)) {
							//2개월더함
							iMnCnt = 2;
						} else {
							//1개월더함
							iMnCnt = 1;
						}

						//1.개월수더함 2.말일자구함 3.영업일로 변경
						sTlmPrfFrftDt = KDateUtil.addMonth(sRealRpyDt, iMnCnt);
						sTlmPrfFrftDt = KDateUtil.getLastDay(sTlmPrfFrftDt);
						sTlmPrfFrftDt = KBizDateUtil
								.getBizDateNextHoliday(sTlmPrfFrftDt);
						rAaRckgDayCu
								.setString("tlm_prf_frft_dt", sTlmPrfFrftDt);
					} else {
						//매각건증 20140401 이후 이자유예 발생건 2개월 유예
						//20180104 수정(주택담보대출 기한이익상실 2개월 유예반영)
						//주택담보대출이라하더라도 기한이익상실일이 2018-01-12 이전건은 기존 1개월유예 유지 
						//(즉 이자유예연체시작일이  2017-12-13 이후건부터 2개월적용 LCCE01_PLS_GCD => M02 신규코드부여됐으나 무조건 2개월이 아님
						if ((KLDataConvertUtil.equals(
								rLonCnrBasInq.getString("lon_sel_dv_cd"), "01") && sAaStDt
								.compareTo("20140228") >= 0)
								|| (KLDataConvertUtil.equals(rLonCnrBasInq
										.getString("hse_scy_ln_yn"), "Y") && sAaStDt
										.compareTo("20171213") >= 0)) {
							//2개월더함
							iMnCnt = 2;
						} else {
							//1개월더함
							iMnCnt = 1;
						}

						//1.개월수더함 2.영업일로 변경
						sTlmPrfFrftDt = KDateUtil.addMonth(sRealRpyDt, iMnCnt);
						sTlmPrfFrftDt = KBizDateUtil
								.getBizDateNextHoliday(sTlmPrfFrftDt);
						rAaRckgDayCu
								.setString("tlm_prf_frft_dt", sTlmPrfFrftDt);

					}
				} else {
					if (KLDataConvertUtil.equals(sTlmPrfFrftPstDvCd1, "D")) {
						//1.14일더함 2.영업일로 변경
						sTlmPrfFrftDt = KDateUtil.addDay(sRealRpyDt, 14);
						sTlmPrfFrftDt = KBizDateUtil
								.getBizDateNextHoliday(sTlmPrfFrftDt);
						rAaRckgDayCu
								.setString("tlm_prf_frft_dt", sTlmPrfFrftDt);

					}
				}

				//만기일과 비교
				if (sExpDt.compareTo(rAaRckgDayCu.getString("tlm_prf_frft_dt")) < 0
						&& sExpDt.compareTo("00010101") > 0) {
					rAaRckgDayCu.setString("tlm_prf_frft_dt", sExpDt);
				}

				rAaRckgDayCu.setString("tlm_prf_frft_dt2",
						rAaRckgDayCu.getString("tlm_prf_frft_dt"));

				rAaRckgDayCu.setString("aa_kd", "2"); //잔액연체
			}
			if ((rAaRckgDayCu.getString("tlm_prf_frft_dt2"))
					.compareTo("20241017") >= 0) {
				if (KLDataConvertUtil.equals(sAaYn, "Y")
						&& (KLDataConvertUtil.equals(
								rLonCnrBasInq.getString("ln_cnr_dv_cd"), "1") || KLDataConvertUtil
								.equals(rLonCnrBasInq.getString("ln_cnr_dv_cd"),
										"2")) // 개인,개인사업자
				) {
					try {

						// =============================================================================
						// ######### GeneralCodeBlock ##연체통지이력건별조회  입력값 세팅
						// =============================================================================
						// [i연체통지이력건별조회]  

						iAaNotiHsCabyInq = new LData();
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iAaNotiHsCabyInq);

						iAaNotiHsCabyInq.setString("ln_no",
								rAffcAaBasInq.getString("ln_no"));
						iAaNotiHsCabyInq.setLong("ln_seq",
								rAffcAaBasInq.getLong("ln_seq"));
						iAaNotiHsCabyInq.setString("aa_occ_dt",
								rAffcAaBasInq.getString("aa_occ_dt"));
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iAaNotiHsCabyInq);
						rAaNotiHsCabyInq = lonCnrBasRcvPatEbc
								.retvAaNotiHsCaby(iAaNotiHsCabyInq); //##연체통지이력건별조회

						// =============================================================================
						// ######### GeneralCodeBlock ##연체통지이력건별조회  결과값 맵핑
						// =============================================================================
						// [r연체통지이력건별조회]

						sSndYn = "Y";

						sLonCsCfDt = rAaNotiHsCabyInq.getString("lon_cs_cf_dt");
						sSndDay = rAaNotiHsCabyInq.getString("snd_prg_dtm");

						if (KLDataConvertUtil.equals(rAaNotiHsCabyInq
								.getString("lon_noti_snd_dv_cd"), "99")) { //홈페이지등재
							sSndDay = rAaNotiHsCabyInq
									.getString("lon_cs_cf_dt");
						}
						if (KStringUtil.trimNisEmpty(sLonCsCfDt)) {
							sLonCsCfDt = "00010101";
						}

						if (KStringUtil.trimNisEmpty(sSndDay)) {
							sSndDay = "00010101";
						}

						if (sLonCsCfDt.compareTo("00010101") > 0) {

							for (int i = 0; i < 10; i++) {

								sLonCsCfDt = KDateUtil.addDay(sLonCsCfDt, 1);

								sLonCsCfDt = KBizDateUtil
										.getBizDateNextHoliday(sLonCsCfDt);

							}

						}
					} catch (LBizNotFoundException e) {

						// =============================================================================
						// ######### GeneralCodeBlock ##기한이익상실공란
						// =============================================================================
						if ((rAaRckgDayCu.getString("tlm_prf_frft_dt2"))
								.compareTo("20241017") >= 0) {

							sSndYn = "N";

							rAaRckgDayCu.setString("tlm_prf_frft_dt", "");

							rAaRckgDayCu.setString("aa_kd", "1"); //잔액연체아님

							//20250618수정: 만기경과건에 대해서는 금액과 상관없이 만기일을 기한이익상실일로 함 
							//if([r여신계약기본조회].[대출금액] >= 30000000 && [s만기일자] > "00010101" && [s만기일자] < [s현재일자]){
							if (sExpDt.compareTo("00010101") > 0
									&& sExpDt.compareTo(sNowDt) < 0) {

								rAaRckgDayCu.setString("tlm_prf_frft_dt",
										sExpDt);

								rAaRckgDayCu.setString("aa_kd", "2"); //잔액연체
							}

						}
					}
					try {

						// =============================================================================
						// ######### GeneralCodeBlock ##채무조정진행상태조회  입력값 세팅
						// =============================================================================
						// [i채무조정진행상태조회]

						iDebMetPrsSsInq = new LData();
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iDebMetPrsSsInq);

						iDebMetPrsSsInq.setString("ln_no",
								rLonCnrBasInq.getString("ln_no"));
						iDebMetPrsSsInq.setLong("ln_seq",
								rLonCnrBasInq.getLong("ln_seq"));
						iDebMetPrsSsInq.setString("aa_occ_dt",
								rAffcAaBasInq.getString("aa_occ_dt"));
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iDebMetPrsSsInq);
						rDebMetPrsSsInq = debMetPrsHisEbc
								.retvDebMetPrsSs(iDebMetPrsSsInq); //##채무조정진행상태조회

						// =============================================================================
						// ######### GeneralCodeBlock ##채무조정기준 기한이익상실일자 산출
						// =============================================================================
						// [r채무조정진행상태조회]

						sDebMetTgtYn = "Y";

						if (KLDataConvertUtil.equals(
								rDebMetPrsSsInq.getString("deb_met_prs_ss_cd"),
								"60")) { //합의서체결
							sDebMetEdDt = rDebMetPrsSsInq.getString("cncls_dt");
						} else {
							sDebMetEdDt = "00010101";
						}
					} catch (LBizNotFoundException e) {

						// =============================================================================
						// ######### GeneralCodeBlock ##채무조정신청내역 없음
						// =============================================================================
						sDebMetTgtYn = "N";
						sDebMetEdDt = "00010101";
					}
					if (((rAaRckgDayCu.getString("tlm_prf_frft_dt"))
							.compareTo("00010101") > 0 && (rAaRckgDayCu
							.getString("tlm_prf_frft_dt2"))
							.compareTo("20241017") >= 0)
							|| KStringUtil.trimNisEmpty(rAaNotiHsCabyInq
									.getString("lon_cs_cf_dt"))) {
						if (KLDataConvertUtil.equals(sSndYn, "Y")) {

							// =============================================================================
							// ######### GeneralCodeBlock ##여신접수기본연기조건변경목록조회  입력값 세팅
							// =============================================================================
							// [i여신접수기본연기조건변경목록조회] 

							iLonRcpBasPspnCondChListInq.setString("lon_rcp_no",
									rLonCnrBasInq.getString("lon_rcp_no"));
							iLonRcpBasPspnCondChListInq.setString(
									"lon_dty_dv_cd", "2"); //연기
							iLonRcpBasPspnCondChListInq.setString("pspn_ss_cd",
									"50"); //승인
							LProtocolInitializeUtil
									.primitiveLMultiInitialize(iLonRcpBasPspnCondChListInq);
							rLonRcpBasPspnCondChListInq = lonRcpBasEbc
									.retvLstLonRcpBasPspnCondCh(iLonRcpBasPspnCondChListInq); //##여신접수기본연기조건변경목록조회
							if (0 < rLonRcpBasPspnCondChListInq.getDataCount()) {

								// =============================================================================
								// ######### GeneralCodeBlock ##여신접수기본연기조건변경목록조회  결과값 맵핑
								// =============================================================================
								// [r여신접수기본연기조건변경목록조회]

								LData tLonRcpBasPspnCondChListInqRslDto = rLonRcpBasPspnCondChListInq
										.getLData(0); //#t여신접수기본연기조건변경목록조회결과Dto

								sPspnCnsDt = tLonRcpBasPspnCondChListInqRslDto
										.getString("cns_dt");

								if (KStringUtil.trimNisEmpty(sPspnCnsDt)) {
									sPspnCnsDt = "00010101";
								}
							}

							// =============================================================================
							// ######### GeneralCodeBlock ##통지기준기한이익상실일자 산출
							// =============================================================================
							// 법해석: 20241017이후 신규(연장)대출의 기한이익상실은 5천만원 미만은 도래하지 않은 원금에대해서는 연체부리를 하지 못한다(기한이익상실일자는 존재)
							// 20241010 현업업무회의 결과(통지관련)

							/* 1.안내장 통지는 고객기준으로 수신하지 않은건 무조건 기한이익상실이 아니다(asis 기준으로 처리하는건 없음)
							 1.1 발송이 되지 않았거나 발송은 됐어도 발송일자가 없는경우   
							 - 안내장기준 정상적인 발송대상이 아닌경우라도 만기경과가 아닌경우는 기한이익상실로 보지 않는다(만기까지는 이자유예만 존재)
							 1.2. 발송은 됐어도 도달일자가 없는경우 기한이익상실이 아니다
							
							 ==> 결국 연체건중 통지테이블에 없는경우는 기한이익상실로 보지않음.

							 */

							// 20241010 현업업무회의 결과(채무조정관련)

							/* 1. 채무조정 합의서체결 건은 채무조정절차 종료일자 일자로 기한이익상실일자 변경
							 2. 합의서체결 이외 (신청,거절,해제 등) 단계는 기존 기한이익상실일자로 유지

							 */

							/* 의뢰서 내용
							 1. 2024.10.17일 이후 신규(연장) 대출의 기한이익이 상실된 경우

							 1.1 대출원금 5천만원 미만 

							 - 납입응당일이 도과한 원(리)금에 대한 정상이자 

							 - 이자에 대한 이자유예연체이자

							 - 납입응당일이 도과한 원금에 대한 연체이자 



							 1.2 대출원금 5천만원 이상 

							 - 납입응당일 도과한 원(리)금에 대한 정상이자

							 - 이자에 대한 이자유예연체이자

							 - 대출잔액에 대한 연체이자 



							 2. ​2024.10.17일 이전 신규(연장) 대출의 기한이익이 상실된 경우 

							 - 납입응당일 도과한 원(리)금에 대한 정상이자

							 - 이자에 대한 이자유예연체이자

							 - 대출잔액에 대한 연체이자



							 ※(참고) 2024.10.17일 이후 연체 대출의 기한이익 상실(예정)일

							 ○ 대출원금 3천만원미만

							 - 기한이익상실 통지 도달시 : 도달후 10영업일 경과

							 - 기한이익상실 통지 미도달시 : 홈페이지 게시후 10영업일 경과

							 ○ 대출원금 3천만원이상

							 - 기한이익상실 통지 발신시

							 */

							if ((rLonCnrBasInq.getBigDecimal("ln_amt"))
									.compareTo(new BigDecimal("30000000")) < 0) {

								if ((rAaRckgDayCu.getString("tlm_prf_frft_dt"))
										.compareTo("00010101") >= 0
										&& (rAaRckgDayCu
												.getString("tlm_prf_frft_dt"))
												.compareTo(sLonCsCfDt) < 0) {
									rAaRckgDayCu.setString("tlm_prf_frft_dt",
											sLonCsCfDt);
								}

								if (KLDataConvertUtil.equals(sLonCsCfDt,
										"00010101")) {

									rAaRckgDayCu.setString("tlm_prf_frft_dt",
											"");
									rAaRckgDayCu.setString("aa_kd", "1"); //잔액연체아님

								} else {

									if ((rLonCnrBasInq.getString("ln_dt"))
											.compareTo("20241017") >= 0
											|| sPspnCnsDt.compareTo("20241017") >= 0) {

										if ((rAaRckgDayCu
												.getString("tlm_prf_frft_dt"))
												.compareTo(sExpDt) > 0) {
											rAaRckgDayCu
													.setString("aa_kd", "2"); //잔액연체
										} else {
											rAaRckgDayCu
													.setString("aa_kd", "1"); //잔액연체아님
										}

									} else {

										rAaRckgDayCu.setString("aa_kd", "2"); //잔액연체
									}
								}

							} else {

								//3천만원이상은 발송여부로 판단
								// 기한이익상실일자는 이전도출된 [r연체기산일산출].[기한이익상실일자] 임
								if (KLDataConvertUtil.equals(sSndDay,
										"00010101")) {

									rAaRckgDayCu.setString("tlm_prf_frft_dt",
											"");
									rAaRckgDayCu.setString("aa_kd", "1"); //잔액연체아님

								} else {

									if ((rLonCnrBasInq.getBigDecimal("ln_amt"))
											.compareTo(new BigDecimal(
													"50000000")) < 0) {

										if ((rLonCnrBasInq.getString("ln_dt"))
												.compareTo("20241017") >= 0
												|| sPspnCnsDt
														.compareTo("20241017") >= 0) {

											if (sNowDt.compareTo(sExpDt) > 0) {
												rAaRckgDayCu.setString("aa_kd",
														"2"); //잔액연체
											} else {
												rAaRckgDayCu.setString("aa_kd",
														"1"); //잔액연체아님
											}

										} else {

											rAaRckgDayCu
													.setString("aa_kd", "2"); //잔액연체
										}

									} else {

										rAaRckgDayCu.setString("aa_kd", "2"); //잔액연체

									}

								}

							}

							rAaRckgDayCu.setString("cs_cf_dt", sLonCsCfDt);

							//이행데이터 - 법시행과 관련없음
							//발송일자 , 도달일자 동일한 경우는 이행데이터로 간주하고 기한이익상실일자2로 고정 . 단, 홈페이지공시 건은 법시행대상
							if (KLDataConvertUtil.equals(
									rAaNotiHsCabyInq.getString("snd_prg_dtm"),
									rAaNotiHsCabyInq.getString("lon_cs_cf_dt"))
									&& KLDataConvertUtil
											.notEquals(
													rAaNotiHsCabyInq
															.getString("lon_noti_snd_dv_cd"),
													"99")) {
								rAaRckgDayCu.setString("tlm_prf_frft_dt",
										rAaRckgDayCu
												.getString("tlm_prf_frft_dt2"));
							}
						}
					}

					// =============================================================================
					// ######### GeneralCodeBlock ##채무조정기준기한이익상실일자 비교
					// =============================================================================
					/*채무조정 진행중 건은 기한이익상실시킬 수 없음 (보류)

					-- 채무조정 
					1. 접수, 절차가 진행중이면 기한이익상실일자 공란
					2. 거절통지, 채무자부동의, 해제되면 기존 기한이익상실일자
					3. 승인되면 채택일자 ,기한이익상실일자 , 채무조정종료일자 둘 다 유효할 때 더 큰 날짜로 기한이익상실 산정 


					 * 연체 건에 한해 신청된 채무조정은 진행단계가 만기경과될 경우, 승인 이전 연체정상화를 진행해야 한다고 함 */

					if (KLDataConvertUtil.equals(sDebMetTgtYn, "Y")) {

						//3.합의서체결
						if (KLDataConvertUtil.equals(
								rDebMetPrsSsInq.getString("deb_met_prs_ss_cd"),
								"60")) {
							if (KLDataConvertUtil.notEquals(
									rAaRckgDayCu.getString("tlm_prf_frft_dt"),
									"")) {
								if (KLDataConvertUtil.notEquals(sDebMetEdDt,
										"00010101")) {
									if ((rAaRckgDayCu
											.getString("tlm_prf_frft_dt"))
											.compareTo(sDebMetEdDt) < 0) {

										rAaRckgDayCu.setString(
												"tlm_prf_frft_dt", sDebMetEdDt);
									}
								}
							}
						}
						//2.거절통지, 채무자부동의, 해제
						else if (KLDataConvertUtil.equals(
								rDebMetPrsSsInq.getString("deb_met_prs_ss_cd"),
								"30")
								|| KLDataConvertUtil.equals(rDebMetPrsSsInq
										.getString("deb_met_prs_ss_cd"), "50")
								|| KLDataConvertUtil.equals(rDebMetPrsSsInq
										.getString("deb_met_prs_ss_cd"), "80")) {

						}
						//1.그 외 나머지 진행중은 기한이익상실일자 공란
						else {
							rAaRckgDayCu.setString("tlm_prf_frft_dt", "");
						}
					}
				}
			}
		} else if (KLDataConvertUtil.equals(iAaRckgDayCu.getString("in_dv"),
				"2") //입력일자기준
		) {

			// =============================================================================
			// ######### GeneralCodeBlock ##실제상환일자, 연체시작일자 세팅
			// =============================================================================
			sRealRpyDt = KBizDateUtil.getBizDateNextHoliday(iAaRckgDayCu
					.getString("in_dt"));
			sAaStDt = KDateUtil.addDay(sRealRpyDt, 1);

			// =============================================================================
			// ######### GeneralCodeBlock ##기한이익상실일자 산출2
			// =============================================================================
			//1개월 유예
			if (KLDataConvertUtil.equals(iAaRckgDayCu.getString("pst_dv_cd"),
					"1")
					|| KLDataConvertUtil.equals(
							iAaRckgDayCu.getString("pst_dv_cd"), "3")) {
				//실납입응당일자가 말일자라면
				if (KDateUtil.isLastDay(sRealRpyDt)) {

					if (KLDataConvertUtil.equals(
							iAaRckgDayCu.getString("pst_dv_cd"), "3")) { //주택담보대출 2개월유예
						iMnCnt = 2;
					} else { //1개월 유예
						iMnCnt = 1;
					}

					//1.개월수변경 2.말일자변경 3.영업일변경
					sTlmPrfFrftDt = KDateUtil.addMonth(sRealRpyDt, iMnCnt);
					sTlmPrfFrftDt = KDateUtil.getLastDay(sTlmPrfFrftDt);
					sTlmPrfFrftDt = KBizDateUtil
							.getBizDateNextHoliday(sTlmPrfFrftDt);
					rAaRckgDayCu.setString("tlm_prf_frft_dt", sTlmPrfFrftDt);
				} else {
					if (KLDataConvertUtil.equals(
							iAaRckgDayCu.getString("pst_dv_cd"), "3")) { //주택담보대출 2개월유예
						iMnCnt = 2;
					} else {
						iMnCnt = 1;
					}
					//1.개월수변경 2.영업일변경
					sTlmPrfFrftDt = KDateUtil.addMonth(sRealRpyDt, iMnCnt);
					sTlmPrfFrftDt = KBizDateUtil
							.getBizDateNextHoliday(sTlmPrfFrftDt);
					rAaRckgDayCu.setString("tlm_prf_frft_dt", sTlmPrfFrftDt);
				}
				//14일 유예
			} else if (KLDataConvertUtil.equals(
					iAaRckgDayCu.getString("pst_dv_cd"), "2")) {
				//1.14일더함 2.영업일로변경
				sTlmPrfFrftDt = KDateUtil.addDay(sRealRpyDt, 14);
				sTlmPrfFrftDt = KBizDateUtil
						.getBizDateNextHoliday(sTlmPrfFrftDt);
				rAaRckgDayCu.setString("tlm_prf_frft_dt", sTlmPrfFrftDt);
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 세팅
		// =============================================================================
		rAaRckgDayCu.setString("real_rpy_dt", sRealRpyDt);
		rAaRckgDayCu.setString("aa_st_dt", sAaStDt);

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_연체기산일산출 END");
			LLog.debug.println(rAaRckgDayCu);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAaRckgDayCu);
		return rAaRckgDayCu;
	}

	/**
	 * 마감일자산출
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055101
	 * @logicalName   월[월말일자확인]변경
	 * @param LData iMonEmthDtCfCh i월월말일자확인변경
	 * @return LData rMonEmthDtCfCh r월월말일자확인변경
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_월[월말일자확인]변경
	 * 
	 */
	public LData chngMonByEmthDtCf(LData iMonEmthDtCfCh) throws LException {
		//#Return 변수 선언 및 초기화
		LData rMonEmthDtCfCh = new LData(); //# r월월말일자확인변경
											//#호출 오퍼레이션에서 사용된 파라미터 초기화
		//#호출 컴포넌트 초기화

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 설정
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■ 월 변경 (말일자확인) INPUT");
			LLog.debug.println(iMonEmthDtCfCh);
		}

		String sOpDt = "00010101"; //#s작업일자

		// =============================================================================
		// ######### CodeValidationBlock ##입력일자 유효성 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			//if (LLog.debug.isEnabled()) {
			//LLog.debug.println("■■■■■ INPUT_입력일자 : " + [i월월말일자확인변경].[입력일자] );
			//LLog.debug.println("■■■■■ INPUT_개월수   : " + [i월월말일자확인변경].[개월수] );
			//}

			if (KLDataConvertUtil.notEquals(
					KDateUtil.isDate(iMonEmthDtCfCh.getString("in_dt")), true)) {
				// 날짜값이 유효하지 않습니다.
				bizExceptionMessage.setBizExceptionMessage("MBRI00001",
						new String[] { "" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##입력일자 개월수만큼 변경하여 작업일자에 세팅
		// =============================================================================
		sOpDt = KDateUtil.addMonth(iMonEmthDtCfCh.getString("in_dt"),
				iMonEmthDtCfCh.getLong("mn_cnt"));
		if (KLDataConvertUtil.equals(
				KDateUtil.isLastDay(iMonEmthDtCfCh.getString("in_dt")), true)) {

			// =============================================================================
			// ######### GeneralCodeBlock ##작업일자 월말일자로 변경
			// =============================================================================
			//if (LLog.debug.isEnabled()) {
			//LLog.debug.println("■■■■■ INPUT 월말일이므로 월말일로 변경 ");
			//}

			sOpDt = KDateUtil.getLastDay(sOpDt);
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 입력
		// =============================================================================
		rMonEmthDtCfCh.setString("otpt_dt", sOpDt);
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■ 월 변경 (말일자확인) OUTPUT_출력일자 : "
					+ rMonEmthDtCfCh.getString("otpt_dt"));
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rMonEmthDtCfCh);
		return rMonEmthDtCfCh;
	}

	/**
	 * 이자유예상환예정잔액조회
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055106
	 * @logicalName   이자유예상환예정잔액조회
	 * @param LData iIrtPstRpyPrgBalInq i이자유예상환예정잔액조회
	 * @return LData rIrtPstRpyPrgBalInq r이자유예상환예정잔액조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_이자유예상환예정잔액조회
	 * 
	 */
	public LData retvIrtPstRpyPrgBal(LData iIrtPstRpyPrgBalInq)
			throws LException {
		//#Return 변수 선언 및 초기화
		LData rIrtPstRpyPrgBalInq = new LData(); //# r이자유예상환예정잔액조회
													//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iLonIrtPstRpyPrgBalAllTotInq = new LData(); //# i여신이자유예상환예정잔액전체합계조회
		LData rLonIrtPstRpyPrgBalAllTotInq = new LData(); //# r여신이자유예상환예정잔액전체합계조회
		LData iLonIrtPstRpyPrgBalExpcPaDayTotInq = new LData(); //# i여신이자유예상환예정잔액예상납입일합계조회
		LData rLonIrtPstRpyPrgBalExpcPaDayTotInq = new LData(); //# r여신이자유예상환예정잔액예상납입일합계조회
		//#호출 컴포넌트 초기화
		LonIrtPstRpyPrgHisEbc lonIrtPstRpyPrgHisEbc = new LonIrtPstRpyPrgHisEbc(); //# 여신이자유예상환예정내역Ebi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 세팅
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_이자유예상환예정잔액조회 - 전역변수 세팅");
			LLog.debug.println(iIrtPstRpyPrgBalInq);
		}

		BigDecimal bdIrtPstRpyAmt = new BigDecimal("0"); //#bd이자유예상환금액
		if (KLDataConvertUtil.equals(
				iIrtPstRpyPrgBalInq.getBigDecimal("ln_bal"),
				iIrtPstRpyPrgBalInq.getBigDecimal("rpy_amt"))) {

			// =============================================================================
			// ######### GeneralCodeBlock ##여신이자유예상환예정잔액합계조회  입력값 세팅
			// =============================================================================
			iLonIrtPstRpyPrgBalAllTotInq.setString("ln_no",
					iIrtPstRpyPrgBalInq.getString("ln_no"));
			iLonIrtPstRpyPrgBalAllTotInq.setLong("ln_seq",
					iIrtPstRpyPrgBalInq.getLong("ln_seq"));
			iLonIrtPstRpyPrgBalAllTotInq.setString("irt_pst_cns_dv_cd",
					iIrtPstRpyPrgBalInq.getString("irt_pst_cns_dv_cd"));

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_이자유예상환예정잔액조회 - 여신이자유예상환예정잔액합계조회  입력값 세팅");
				LLog.debug.println(iLonIrtPstRpyPrgBalAllTotInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iLonIrtPstRpyPrgBalAllTotInq);
			rLonIrtPstRpyPrgBalAllTotInq = lonIrtPstRpyPrgHisEbc
					.retvLonIrtPstRpyPrgBalByAll(iLonIrtPstRpyPrgBalAllTotInq); //##여신이자유예상환예정잔액[전체]합계조회

			// =============================================================================
			// ######### GeneralCodeBlock ##여신이자유예상환예정잔액합계조회  결과값 맵핑
			// =============================================================================
			bdIrtPstRpyAmt = rLonIrtPstRpyPrgBalAllTotInq
					.getBigDecimal("rpy_prg_bal_tot_amt");

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ 이자유예상환예정잔액합계조회 OUTPUT 상환예정잔액합계금액");
				LLog.debug.println(bdIrtPstRpyAmt);
			}
		} else {

			// =============================================================================
			// ######### GeneralCodeBlock ##여신이자유예상환예정잔액[예상납입일]합계조회  입력값 세팅
			// =============================================================================
			iLonIrtPstRpyPrgBalExpcPaDayTotInq.setString("ln_no",
					iIrtPstRpyPrgBalInq.getString("ln_no"));
			iLonIrtPstRpyPrgBalExpcPaDayTotInq.setLong("ln_seq",
					iIrtPstRpyPrgBalInq.getLong("ln_seq"));
			iLonIrtPstRpyPrgBalExpcPaDayTotInq.setString("irt_pst_cns_dv_cd",
					iIrtPstRpyPrgBalInq.getString("irt_pst_cns_dv_cd"));
			iLonIrtPstRpyPrgBalExpcPaDayTotInq.setString(
					"rpy_prg_dt",
					KDateUtil.addDay(
							iIrtPstRpyPrgBalInq.getString("cmpe_ls_dt"), 1));

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_이자유예상환예정잔액조회 - 여신이자유예상환예정잔액expcPaDay합계조회  입력값 세팅");
				LLog.debug.println(iLonIrtPstRpyPrgBalExpcPaDayTotInq);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iLonIrtPstRpyPrgBalExpcPaDayTotInq);
			rLonIrtPstRpyPrgBalExpcPaDayTotInq = lonIrtPstRpyPrgHisEbc
					.retvLonIrtPstRpyPrgBalByExpcPaDay(iLonIrtPstRpyPrgBalExpcPaDayTotInq); //##여신이자유예상환예정잔액[예상납입일]합계조회

			// =============================================================================
			// ######### GeneralCodeBlock ##여신이자유예상환예정잔액[예상납입일]합계조회  결과값 맵핑
			// =============================================================================
			bdIrtPstRpyAmt = rLonIrtPstRpyPrgBalExpcPaDayTotInq
					.getBigDecimal("rpy_prg_bal_tot_amt");

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_이자유예상환예정잔액조회 - 여신이자유예상환예정잔액expcPaDay합계조회  결과값 맵핑"
								+ bdIrtPstRpyAmt);
				LLog.debug.println(bdIrtPstRpyAmt);
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 세팅
		// =============================================================================
		rIrtPstRpyPrgBalInq.setBigDecimal("irt_pst_rpy_amt", bdIrtPstRpyAmt);

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_이자유예상환예정잔액조회 - 결과값 세팅");
			LLog.debug.println(rIrtPstRpyPrgBalInq);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rIrtPstRpyPrgBalInq);
		return rIrtPstRpyPrgBalInq;
	}

	/**
	 * 입력이수최종일자산출
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055107
	 * @logicalName   입력이수최종일자산출
	 * @param LData iInCmpeLsDtCu i입력이수최종일자산출
	 * @return LData rInCmpeLsDtCu r입력이수최종일자산출
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_입력이수최종일자산출
	 * 
	 */
	public LData cmptInCmpeLsDt(LData iInCmpeLsDtCu) throws LException {
		//#Return 변수 선언 및 초기화
		LData rInCmpeLsDtCu = new LData(); //# r입력이수최종일자산출
											//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData rDtmGnoInq = new LData(); //# r일시채번조회
										//#호출 컴포넌트 초기화
		LonCnrBasRcvPatEbc lonCnrBasRcvPatEbc = new LonCnrBasRcvPatEbc(); //# 여신계약기본수납파트Ebi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 설정
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■■■ 입력이수최종일자산출 START ");
			LLog.debug.println("■■■■■ 001_전역변수 SET ");
			LLog.debug.println(iInCmpeLsDtCu);
		}

		String sOpDt = "00010101"; //#s작업일자
		String sEmthDt = "00010101"; //#s월말일자
		String sStDt = "00010101"; //#s시작일자
		String sCmpeLsDt = (((LData) iInCmpeLsDtCu.get("inCmpeLsDtCuInDto"))
				.getString("in_cmpe_ls_dt")); //#s이수최종일자

		// =============================================================================
		// ######### CodeValidationBlock ##입력값 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■■■ ACSD_입력이수최종일자산출 - 입력값 검증");
				LLog.debug.println(iInCmpeLsDtCu);
			}

			if (KLDataConvertUtil.notEquals(KDateUtil
					.isDate((((LData) iInCmpeLsDtCu.get("inCmpeLsDtCuInDto"))
							.getString("in_cmpe_ls_dt"))), true)) {
				// 날짜값이 유효하지 않습니다.
				bizExceptionMessage.setBizExceptionMessage("MBRI00001",
						new String[] { "" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}

		//인스펙션때문에 걸어놓음..
		{
			rDtmGnoInq = lonCnrBasRcvPatEbc.retvDtmGno(); //##일시채번조회
		}
		if (KLDataConvertUtil.equals((((LData) iInCmpeLsDtCu
				.get("inCmpeLsDtCuInDto")).getString("in_cmpe_ls_dv")), "1") //이수최종월
		) {
			if (KLDataConvertUtil.equals((((LData) iInCmpeLsDtCu
					.get("inCmpeLsDtCuLonCnrBasInDto"))
					.getString("lon_pa_mth_cd")), "10") //수납방법이 창구인 경우
			) {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신계약기본.납입응당일자로 월말일자 산출
				// =============================================================================
				sOpDt = (((LData) iInCmpeLsDtCu
						.get("inCmpeLsDtCuLonCnrBasInDto"))
						.getString("pa_fsr_dt"));
				sEmthDt = KDateUtil.getLastDay((((LData) iInCmpeLsDtCu
						.get("inCmpeLsDtCuLonCnrBasInDto"))
						.getString("pa_fsr_dt")));

				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■■■ 100_작업일자 / 월말일자 ");
					LLog.debug.println(sOpDt);
					LLog.debug.println(sEmthDt);
				}
				if (KLDataConvertUtil.equals(sEmthDt, (((LData) iInCmpeLsDtCu
						.get("inCmpeLsDtCuLonCnrBasInDto"))
						.getString("pa_fsr_dt"))) //여신계약기본.납입응당일자 == 여신계약기본.납입응당일자로 월말일자
				) {

					// =============================================================================
					// ######### GeneralCodeBlock ##전역변수.작업일자에 입력이수최종일자로 월말일자 세팅
					// =============================================================================
					sOpDt = KDateUtil.getLastDay((((LData) iInCmpeLsDtCu
							.get("inCmpeLsDtCuInDto"))
							.getString("in_cmpe_ls_ym"))
							+ "01");

					//LLog.debug.println("■■■■■ 101_작업일자  : " + [s작업일자]);
				} else {

					// =============================================================================
					// ######### GeneralCodeBlock ##입력이수최종일자가 작업일자보다 크면 작업일자 변경
					// =============================================================================
					//String [s임시입력이수최종년월] = [i입력이수최종일자산출].[입력이수최종일자산출입력Dto].[입력이수최종년월];

					sOpDt = (((LData) iInCmpeLsDtCu.get("inCmpeLsDtCuInDto"))
							.getString("in_cmpe_ls_ym"))
							+ KStringUtil.substring(sOpDt, 6, 8);

					String sTempEmthDt = KDateUtil
							.getLastDay((((LData) iInCmpeLsDtCu
									.get("inCmpeLsDtCuInDto"))
									.getString("in_cmpe_ls_ym"))
									+ "01"); //#s임시월말일자
					//LLog.debug.println("■■■■■ 102_작업일자      : " + [s작업일자]);
					//LLog.debug.println("■■■■■ 102_임시월말일자  : " + [s임시월말일자]);

					//말일초과 시 월말일로 변경
					if (sOpDt.compareTo(sTempEmthDt) > 0) {
						sOpDt = sTempEmthDt;
						//	LLog.debug.println("■■■■■ 102_월말일 초과하여 월말일로 변경 " + [s작업일자]);
					}
				}
			} else {// 여신계약기본.수납방법 == 자동이체
			// 여신계약기본.수납방법 == 자동이체
				if ((((LData) iInCmpeLsDtCu.get("inCmpeLsDtCuLonCnrBasInDto"))
						.getString("rcv_trs_day")).compareTo("30") >= 0) {

					// =============================================================================
					// ######### GeneralCodeBlock ##전역변수.작업일자에 입력이수최종일자로 월말일자 세팅
					// =============================================================================
					sOpDt = KDateUtil.getLastDay((((LData) iInCmpeLsDtCu
							.get("inCmpeLsDtCuInDto"))
							.getString("in_cmpe_ls_ym"))
							+ "01");

					//LLog.debug.println("■■■■■ 103_작업일자      : " + [s작업일자]);
				} else {

					// =============================================================================
					// ######### GeneralCodeBlock ##전역변수.년,월,일에 입력이수최종일자로 년, 월 세팅 일에 여신계약기본.수납이체일 세팅
					// =============================================================================
					sOpDt = KStringUtil.substring((((LData) iInCmpeLsDtCu
							.get("inCmpeLsDtCuInDto"))
							.getString("in_cmpe_ls_ym")), 0, 6)
							+ (((LData) iInCmpeLsDtCu
									.get("inCmpeLsDtCuLonCnrBasInDto"))
									.getString("rcv_trs_day"));

					//LLog.debug.println("■■■■■ 104_작업일자      : " + [s작업일자]);
				}
			}
			if ((((LData) iInCmpeLsDtCu.get("inCmpeLsDtCuLonCnrBasInDto"))
					.getString("exp_dt")).compareTo((((LData) iInCmpeLsDtCu
					.get("inCmpeLsDtCuInDto")).getString("rei_dt"))) <= 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.작업일자 = 전역변수.영수일자(전역변수.영수일자 < 전역변수.작업일자)
				// =============================================================================
				if ((((LData) iInCmpeLsDtCu.get("inCmpeLsDtCuInDto"))
						.getString("rei_dt")).compareTo(sOpDt) < 0) {
					//	LLog.debug.println("■■■■■ 105_1_작업일자      : " + [s작업일자]);
					sOpDt = (((LData) iInCmpeLsDtCu.get("inCmpeLsDtCuInDto"))
							.getString("rei_dt"));
				}

				//LLog.debug.println("■■■■■ 105_2_작업일자      : " + [s작업일자]);
			} else {

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.작업일자 = 여신계약기본.연기만기일자(여신계약기본.연기만기일자 < 전역변수.작업일자)
				// =============================================================================
				if ((((LData) iInCmpeLsDtCu.get("inCmpeLsDtCuLonCnrBasInDto"))
						.getString("exp_dt")).compareTo(sOpDt) < 0) {
					//	LLog.debug.println("■■■■■ 106_1_작업일자      : " + [s작업일자]);
					sOpDt = (((LData) iInCmpeLsDtCu
							.get("inCmpeLsDtCuLonCnrBasInDto"))
							.getString("exp_dt"));
				}
				//LLog.debug.println("■■■■■ 106_2_작업일자      : " + [s작업일자]);
			}
			if (KLDataConvertUtil.equals((((LData) iInCmpeLsDtCu
					.get("inCmpeLsDtCuInDto"))
					.getString("in_cmpe_ls_dt_op_dv_cd")), "01") //작업구분코드 == '01' // 후취
			) {
				if (KLDataConvertUtil.equals(
						KStringUtil.substring(sOpDt, 6, 8), "01")) {

					// =============================================================================
					// ######### CodeValidationBlock ##작업일자 날짜유효성 Check
					// =============================================================================
					{
						LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
						boolean existInvalidElement = false;

						//---------------------------------------------------------------------------------------------------------------------
						// 작업일자 Vaild Chk
						//---------------------------------------------------------------------------------------------------------------------
						if (KLDataConvertUtil.notEquals(
								KDateUtil.isDate(sOpDt), true)) {
							// 날짜값이 유효하지 않습니다.
							bizExceptionMessage.setBizExceptionMessage(
									"MZZZ00194",
									new String[] { "입력이수최종일자산출 중 이수최종일자" });
							existInvalidElement = true;
						}

						if (existInvalidElement) {
							bizExceptionMessage.throwBizException();
						}
					}

					// =============================================================================
					// ######### GeneralCodeBlock ##전역변수.작업일자로 +1개월 산출하여 전역변수.작업일자에 세팅
					// =============================================================================
					sOpDt = KDateUtil.addMonth(sOpDt, 1);

					//LLog.debug.println("■■■■■ 107_작업일자      : " + [s작업일자]);
				}

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.시작일자에 전역변수.작업일자 세팅
				// =============================================================================
				sStDt = sOpDt;

				//LLog.debug.println("■■■■■ 108_시작일자      : " + [s시작일자]);

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.이수최종일자 = 전역변수.작업일자로 전일자 산출
				// =============================================================================
				sCmpeLsDt = KDateUtil.addDay(sOpDt, -1);

				//LLog.debug.println("■■■■■ 109_이수최종일자  : " + [s이수최종일자]);
			} else if (KLDataConvertUtil.equals((((LData) iInCmpeLsDtCu
					.get("inCmpeLsDtCuInDto"))
					.getString("in_cmpe_ls_dt_op_dv_cd")), "02") //작업구분코드 == '02' // 선취
			) {

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.입력이수최종일자 = 전역변수.작업일자
				// =============================================================================
				sCmpeLsDt = sOpDt;

				//LLog.debug.println("■■■■■ 110_이수최종일자  : " + [s이수최종일자]);
			}
			if (sCmpeLsDt
					.compareTo((((LData) iInCmpeLsDtCu
							.get("inCmpeLsDtCuLonCnrBasInDto"))
							.getString("cmpe_ls_dt"))) < 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.이수최종일자 = 여신계약기본.이수최종일자
				// =============================================================================
				sCmpeLsDt = (((LData) iInCmpeLsDtCu
						.get("inCmpeLsDtCuLonCnrBasInDto"))
						.getString("cmpe_ls_dt"));

				//LLog.debug.println("■■■■■ 111_이수최종일자  : " + [s이수최종일자]);
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 입력
		// =============================================================================
		rInCmpeLsDtCu.setString("st_dt", sStDt); //시작일자는 후취 회차구할때 사용함.
		rInCmpeLsDtCu.setString("in_cmpe_ls_dt", sCmpeLsDt);

		LLog.debug.println("■■■■■ ACSD_입력이수최종일자산출 - 결과값 입력");
		LLog.debug.println(rInCmpeLsDtCu);
		LProtocolInitializeUtil.primitiveLMultiInitialize(rInCmpeLsDtCu);
		return rInCmpeLsDtCu;
	}

	/**
	 * 입력회차산출
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055108
	 * @logicalName   입력회차산출
	 * @param LData iInTimCu i입력회차산출
	 * @return LData rInTimCu r입력회차산출
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_입력회차산출
	 * 
	 */
	public LData cmptInTim(LData iInTimCu) throws LException {
		//#Return 변수 선언 및 초기화
		LData rInTimCu = new LData(); //# r입력회차산출
										//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData rDtmGnoInq = new LData(); //# r일시채번조회
		LData iMonEmthDtCfCh = new LData(); //# i월월말일자확인변경
		LData rMonEmthDtCfCh = new LData(); //# r월월말일자확인변경
											//#호출 컴포넌트 초기화
		LonCnrBasRcvPatEbc lonCnrBasRcvPatEbc = new LonCnrBasRcvPatEbc(); //# 여신계약기본수납파트Ebi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 설정
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■■■ 입력회차산출 START ");
			LLog.debug.println("■■■■■ 001_전역변수 SET ");
			LLog.debug.println(iInTimCu);
		}

		String sOpDt = (((LData) iInTimCu.get("inTimCuLonCnrBasInDto"))
				.getString("pa_fsr_dt")); //#s작업일자
		String sOpYr = ""; //#s작업년도
		String sOpMon = ""; //#s작업월
		String sOpDay = ""; //#s작업일

		String sOpDtK = "00010101"; //#s작업일자K
		//String [s작업년도K] = "";
		//String [s작업월K] = "";
		String sOpDayK = ""; //#s작업일K

		String sDfrmPdEdDt = "00010101"; //#s거치기간종료일자
		long lInslPaTim = 0; //#l할부납입회차

		String sEdSgn = " "; //#s종료신호

		String sTempOpDt = "00010101"; //#s임시작업일자
		String sTempOpYr = ""; //#s임시작업년도
		String sTempOpMon = ""; //#s임시작업월
		String sTempOpDay = ""; //#s임시작업일

		long lTempTim = 0; //#l임시회차

		//LLog.debug.println("■■■■■ 작업일자 (납입응당일자) : " + [s작업일자]);

		// =============================================================================
		// ######### CodeValidationBlock ##입력값 검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			LLog.debug.println("■■■■■ ACSD_입력회차산출 - 입력값 검증");
			LLog.debug.println((LData) iInTimCu.get("inTimCuInDto"));

			if (KLDataConvertUtil.notEquals(KDateUtil.isDate((((LData) iInTimCu
					.get("inTimCuInDto")).getString("st_dt"))), true)) {
				// 날짜값이 유효하지 않습니다.
				bizExceptionMessage.setBizExceptionMessage("MBRI00001",
						new String[] { "" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 초기화
		// =============================================================================
		rInTimCu.setLong("insl_pa_tim", (((LData) iInTimCu.get("inTimCuInDto"))
				.getLong("in_insl_pa_tim")));
		rInTimCu.setString("rsl_cd", "00"); //00 정상, 01 미산출대상

		//인스펙션때문에 걸어놓음..
		{
			rDtmGnoInq = lonCnrBasRcvPatEbc.retvDtmGno(); //##일시채번조회
		}
		if (KLDataConvertUtil.equals((((LData) iInTimCu.get("inTimCuInDto"))
				.getString("in_cmpe_ls_dv")), "1") //입력이수최종구분 == 입력이수최종월
		) {
			if (KLDataConvertUtil.notEquals((((LData) iInTimCu
					.get("inTimCuLonCnrBasInDto")).getString("rpy_mth_cd")),
					"03")
					&& KLDataConvertUtil.notEquals((((LData) iInTimCu
							.get("inTimCuLonCnrBasInDto"))
							.getString("rpy_mth_cd")), "05") // 상환방법코드 03-원리금균등 05-거치후원리금균등
			) {

				// =============================================================================
				// ######### GeneralCodeBlock ##결과코드 세팅(산출 대상이 아닙니다.)
				// =============================================================================
				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■■■ 회차 산출대상 아님 ");
				}
				rInTimCu.setString("rsl_cd", "01");
				rInTimCu.setLong("insl_pa_tim", (((LData) iInTimCu
						.get("inTimCuInDto")).getLong("in_insl_pa_tim")));

				LLog.debug.println("■■■■■ RETURN ! ");
				//LLog.debug.println("■■■■■ OUTPUT_결과코드     : " + [r입력회차산출].[결과코드] );
				//LLog.debug.println("■■■■■ OUTPUT_할부납입회차 : " + [r입력회차산출].[할부납입회차] );

				// =============================================================================
				// ######### GeneralCodeBlock ##return
				// =============================================================================
				LProtocolInitializeUtil.primitiveLMultiInitialize(rInTimCu);
				return rInTimCu;
			}
			if (KLDataConvertUtil.equals((((LData) iInTimCu
					.get("inTimCuLonCnrBasInDto")).getString("rpy_mth_cd")),
					"05")
					&& KLDataConvertUtil.equals((((LData) iInTimCu
							.get("inTimCuLonCnrBasInDto"))
							.getLong("insl_pa_tim")), 0) //거치후원리금균등분할이고 할부납입이 없는경우
			) {

				// =============================================================================
				// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅 - 대출일자 + 거치기간
				// =============================================================================
				iMonEmthDtCfCh.setString("in_dt", (((LData) iInTimCu
						.get("inTimCuLonCnrBasInDto")).getString("ln_dt")));
				iMonEmthDtCfCh.setLong("mn_cnt", (((LData) iInTimCu
						.get("inTimCuLonCnrBasInDto")).getLong("dfrm_mcn")));
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iMonEmthDtCfCh);
				rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

				// =============================================================================
				// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑 - 전역변수 거치기간종료일자
				// =============================================================================
				sDfrmPdEdDt = rMonEmthDtCfCh.getString("otpt_dt");

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.작업일자=납입응당일자, 작업일자K=거치기간종료일자 세팅
				// =============================================================================
				sOpDt = (((LData) iInTimCu.get("inTimCuLonCnrBasInDto"))
						.getString("pa_fsr_dt"));
				sOpDtK = sDfrmPdEdDt;

				//LLog.debug.println("■■■■■ 100_작업일자  : " + [s작업일자]);
				//LLog.debug.println("■■■■■ 100_작업일자K : " + [s작업일자K]);
				if (sOpDt.compareTo(sDfrmPdEdDt) <= 0
						|| KLDataConvertUtil.equals(
								KStringUtil.substring(sOpDtK, 0, 6),
								KStringUtil.substring(sOpDt, 0, 6))) {
					while (sOpDt.compareTo(sDfrmPdEdDt) < 0) {

						// =============================================================================
						// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅 - 작업일자 + 1개월
						// =============================================================================
						// [i월월말일자확인변경]
						iMonEmthDtCfCh.setString("in_dt", sOpDt);
						iMonEmthDtCfCh.setLong("mn_cnt", 1);
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iMonEmthDtCfCh);
						rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

						// =============================================================================
						// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑 - 작업일자에 결과값 입력
						// =============================================================================
						sOpDt = rMonEmthDtCfCh.getString("otpt_dt");
						//LLog.debug.println("■■■■■ 101_작업일자  : " + [s작업일자]);
					}

					// =============================================================================
					// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅 - 작업일자 + 1개월
					// =============================================================================
					iMonEmthDtCfCh.setString("in_dt", sOpDt);
					iMonEmthDtCfCh.setLong("mn_cnt", 1);
					LProtocolInitializeUtil
							.primitiveLMultiInitialize(iMonEmthDtCfCh);
					rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

					// =============================================================================
					// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑 - 거치 후 최초 납입응당일자 Set
					// =============================================================================
					sOpDt = rMonEmthDtCfCh.getString("otpt_dt");
					//LLog.debug.println("■■■■■ 102_작업일자  : " + [s작업일자]);
				} else {

					// =============================================================================
					// ######### GeneralCodeBlock ##로컬변수 Set - 작업일수
					// =============================================================================
					sOpDayK = KStringUtil.substring(sOpDtK, 6, 8);
					sOpDay = KStringUtil.substring(sOpDt, 6, 8);

					//LLog.debug.println("■■■■■ 103_작업일자  : " + [s작업일자]);
					//LLog.debug.println("■■■■■ 103_작업일자K : " + [s작업일자K]);
					//LLog.debug.println("■■■■■ 103_작업일    : " + [s작업일]);
					//LLog.debug.println("■■■■■ 103_작업일K   : " + [s작업일K]);
					if (sOpDayK.compareTo(sOpDay) > 0) {

						// =============================================================================
						// ######### GeneralCodeBlock ##거치후 최초 납입응당일자 산출 - 전역변수.작업일자 = 전역변수.거치기간종료일자 ~ 전역변수.작업일자 일수 년월일 산출
						// =============================================================================
						String sStDt = sDfrmPdEdDt; //#s시작일자
						String sEdDt = sOpDt; //#s종료일자
						int iDiffMnCnt = 0; //#i차이개월수

						if (sStDt.compareTo(sEdDt) > 0) {
							sStDt = sOpDt;
							sEdDt = sDfrmPdEdDt;
						}

						iDiffMnCnt = KDateUtil.getYMDInterval(sStDt, sEdDt, 1)[1];
						//LLog.debug.println("■■■■■ 104_s시작일자 ~ s종료일자 / 차이개월수 : " + [s시작일자] + "~" + [s종료일자] + "/" +[i차이개월수] );
						if (iDiffMnCnt < 1) {

							// =============================================================================
							// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅 - 작업일자 +1개월
							// =============================================================================
							iMonEmthDtCfCh.setString("in_dt", sOpDt);
							iMonEmthDtCfCh.setLong("mn_cnt", 1);
							LProtocolInitializeUtil
									.primitiveLMultiInitialize(iMonEmthDtCfCh);
							rMonEmthDtCfCh = this
									.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

							// =============================================================================
							// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑 - 작업일자에 Set (거치 후 최초 납입응당일자)
							// =============================================================================
							sOpDt = rMonEmthDtCfCh.getString("otpt_dt");

							//LLog.debug.println("■■■■■ 105_작업일자  : " + [s작업일자]);
						}
					}
				}
			} else {

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.작업일자 = 여신계약기본.납입응당일자
				// =============================================================================
				sOpDt = (((LData) iInTimCu.get("inTimCuLonCnrBasInDto"))
						.getString("pa_fsr_dt"));

				//LLog.debug.println("■■■■■ 106_작업일자  : " + [s작업일자]);
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##전역변수.할부납입회차 = 여신계약기본.할부납입회차
			// =============================================================================
			lInslPaTim = (((LData) iInTimCu.get("inTimCuLonCnrBasInDto"))
					.getLong("insl_pa_tim"));
			lTempTim = 0; //SV_OTS_FLAG
			//LLog.debug.println("■■■■■ 107_할부납입회차  : " + [l할부납입회차]);
			if ((((LData) iInTimCu.get("inTimCuLonCnrBasInDto"))
					.getString("cmpe_ls_dt")).compareTo(KDateUtil.addDay(sOpDt,
					-1)) >= 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##l회차 = -1
				// =============================================================================
				lTempTim = -1;

				//LLog.debug.println("■■■■■ 108_임시회차 -1로 변경" );
			}
			if (sOpDt.compareTo((((LData) iInTimCu.get("inTimCuInDto"))
					.getString("st_dt"))) <= 0) {

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.할부납입회차 산출
				// =============================================================================
				sEdSgn = " ";
				sOpYr = KStringUtil.substring(sOpDt, 0, 4);
				sOpMon = KStringUtil.substring(sOpDt, 4, 6);
				sOpDay = KStringUtil.substring(sOpDt, 6, 8);

				sTempOpDt = (((LData) iInTimCu.get("inTimCuInDto"))
						.getString("st_dt"));
				sTempOpYr = KStringUtil.substring(sTempOpDt, 0, 4);
				sTempOpMon = KStringUtil.substring(sTempOpDt, 4, 6);
				sTempOpDay = KStringUtil.substring(sTempOpDt, 6, 8);

				//LLog.debug.println("■■■■■ 109_작업일자      : " + [s작업일자]);
				//LLog.debug.println("■■■■■ 109_작업년도      : " + [s작업년도]);
				//LLog.debug.println("■■■■■ 109_작업월        : " + [s작업월]);
				//LLog.debug.println("■■■■■ 109_작업일        : " + [s작업일]);
				//LLog.debug.println("■■■■■ 109_임시작업일자  : " + [s임시작업일자]);
				//LLog.debug.println("■■■■■ 109_임시작업년도  : " + [s임시작업년도]);
				//LLog.debug.println("■■■■■ 109_임시작업월    : " + [s임시작업월]);
				//LLog.debug.println("■■■■■ 109_임시작업일    : " + [s임시작업일]);
				while (sOpYr.compareTo(sTempOpYr) <= 0
						&& KLDataConvertUtil.equals(sEdSgn, " ")
						&& lInslPaTim < (((LData) iInTimCu
								.get("inTimCuLonCnrBasInDto"))
								.getLong("insl_ttim"))) {
					if (sOpDt.compareTo((((LData) iInTimCu
							.get("inTimCuLonCnrBasInDto"))
							.getString("cmpe_ls_dt"))) > 0) {

						// =============================================================================
						// ######### GeneralCodeBlock ##할부납입회차 변경
						// =============================================================================
						lInslPaTim = lInslPaTim + lTempTim + 1;
						lTempTim = 0;

						//LLog.debug.println("■■■■■ 110_할부납입회차   : " + [l할부납입회차]);
					}

					// =============================================================================
					// ######### GeneralCodeBlock ##월[월말일자확인]변경  입력값 세팅 - 작업일자 + 1
					// =============================================================================
					iMonEmthDtCfCh.setString("in_dt", sOpDt);
					iMonEmthDtCfCh.setLong("mn_cnt", 1);
					LProtocolInitializeUtil
							.primitiveLMultiInitialize(iMonEmthDtCfCh);
					rMonEmthDtCfCh = this.chngMonByEmthDtCf(iMonEmthDtCfCh); //##월[월말일자확인]변경

					// =============================================================================
					// ######### GeneralCodeBlock ##월[월말일자확인]변경  결과값 맵핑
					// =============================================================================
					sOpDt = rMonEmthDtCfCh.getString("otpt_dt");

					sOpYr = KStringUtil.substring(sOpDt, 0, 4);
					sOpMon = KStringUtil.substring(sOpDt, 4, 6);
					sOpDay = KStringUtil.substring(sOpDt, 6, 8);

					//LLog.debug.println("■■■■■ 111_작업일자   : " + [s작업일자]);
					if (KLDataConvertUtil.equals(sOpYr, sTempOpYr)
							&& sOpMon.compareTo(sTempOpMon) > 0) {

						// =============================================================================
						// ######### GeneralCodeBlock ##종료신호설정
						// =============================================================================
						sEdSgn = "1";
						//LLog.debug.println("■■■■■ 112_루프 종료설정");
					}
				}
			}
			if (lInslPaTim > (((LData) iInTimCu.get("inTimCuLonCnrBasInDto"))
					.getLong("insl_ttim"))) {

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수.할부납입회차 = 여신계약기본.총할부회차
				// =============================================================================
				lInslPaTim = (((LData) iInTimCu.get("inTimCuLonCnrBasInDto"))
						.getLong("insl_ttim"));

				//LLog.debug.println("■■■■■ 113_할부납입회차 : " +[l할부납입회차]);
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##결과값 입력
			// =============================================================================
			rInTimCu.setLong("insl_pa_tim", lInslPaTim);
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과로그
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■  ACSD_입력회차산출 - 결과로그");
			LLog.debug.println(rInTimCu);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rInTimCu);
		return rInTimCu;
	}

	/**
	 * 전액상환계산건에 대해서 소관기관 소속원에게 알림을 전송함
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055115
	 * @logicalName   전액상환소관기관통보전송
	 * @param LData iAamRpyJrsdOgDspcTsm i전액상환소관기관통보전송
	 * @return LData rAamRpyJrsdOgDspcTsm r전액상환소관기관통보전송
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_전액상환소관기관통보전송
	 * 
	 */
	public LData tsmAamRpyJrsdOgDspc(LData iAamRpyJrsdOgDspcTsm)
			throws LException {
		//#Return 변수 선언 및 초기화
		LData rAamRpyJrsdOgDspcTsm = new LData(); //# r전액상환소관기관통보전송

		//#페이징 처리시 다음페이지 여부 관련 변수 선언 및 초기화
		int _total_row_cnt = 0;
		int _total_page_cnt = 0;
		//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iLonCnrBasInq = new LData(); //# i여신계약기본조회
		LData rLonCnrBasInq = new LData(); //# r여신계약기본조회
		LData iLonAcnyOrzInq = new LData(); //# i여신경리조직조회
		LData rLonAcnyOrzInq = new LData(); //# r여신경리조직조회
		LData iRpyInqyMgtLnNoListInq = new LData(); //# i상환문의관리대출번호목록조회
		LMultiData rRpyInqyMgtLnNoListInq = new LMultiData(); //# r상환문의관리대출번호목록조회
		LData iIgCsImInq = new LData(); //# i통합고객정보조회
		LData rIgCsImInq = new LData(); //# r통합고객정보조회
		LData iLonOfOmListInq = new LData(); //# i여신제공조직원목록조회
		LMultiData rLonOfOmListInq = new LMultiData(); //# r여신제공조직원목록조회
		LData iEmalOmSndReg = new LData(); //# i이메일조직원발송등록
		LData rDtmGnoInq = new LData(); //# r일시채번조회
		LData iRpyInqyMgtHisReg = new LData(); //# i상환문의관리내역등록
		long rRpyInqyMgtHisReg = 0; //# r상환문의관리내역등록
		LData icOrzOrldImInq = new LData(); //# ic조직조직장정보조회
		LMultiData rcOrzOrldImInq = new LMultiData(); //# rc조직조직장정보조회
		LData iCsCtplInq = new LData(); //# i고객연락처조회
		LData rCsCtplInq = new LData(); //# r고객연락처조회
		LData iChtrGeSndReg = new LData(); //# i문자안내발송등록
		LMultiData rChtrGeSndReg = new LMultiData(); //# r문자안내발송등록
		//#호출 컴포넌트 초기화
		LonCnrBasEbc lonCnrBasEbc = new LonCnrBasEbc(); //# 여신계약기본Ebi
		LonAcnyOrzInqCpbc lonAcnyOrzInqCpbc = new LonAcnyOrzInqCpbc(); //# 여신경리조직조회Cpbi
		RpyInqyMgtHisEbc rpyInqyMgtHisEbc = new RpyInqyMgtHisEbc(); //# 상환문의관리내역Ebi
		CsCommImIbc csCommImIbc = new CsCommImIbc(); //# 고객공통정보Ibi
		OmBasEbc omBasEbc = new OmBasEbc(); //# 조직원기본Ebi
		IgGeMgtIbc igGeMgtIbc = new IgGeMgtIbc(); //# 통합안내관리Ibi
		LonCnrBasRcvPatEbc lonCnrBasRcvPatEbc = new LonCnrBasRcvPatEbc(); //# 여신계약기본수납파트Ebi
		HmreMgtIbc hmreMgtIbc = new HmreMgtIbc(); //# 인사관리Ibi

		// =============================================================================
		// ######### CodeValidationBlock ##입력검증
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			if (KStringUtil.trimNisEmpty(iAamRpyJrsdOgDspcTsm
					.getString("ln_no"))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "대출번호" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 생성
		// =============================================================================
		String sRcvrEmalAdr = ""; //#s수신자이메일주소
		try {

			// =============================================================================
			// ######### GeneralCodeBlock ##여신계약기본조회  입력값 세팅
			// =============================================================================
			iLonCnrBasInq.setString("ln_no",
					iAamRpyJrsdOgDspcTsm.getString("ln_no"));
			iLonCnrBasInq.setLong("ln_seq",
					iAamRpyJrsdOgDspcTsm.getLong("ln_seq"));

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_전액상환소관기관통보전송 - 여신계약기본조회  입력값 세팅");
				LLog.debug.println(iLonCnrBasInq);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(iLonCnrBasInq);
			rLonCnrBasInq = lonCnrBasEbc.retvLonCnrBas(iLonCnrBasInq); //##여신계약기본조회
		} catch (LBizNotFoundException e) {

			// =============================================================================
			// ######### ExceptionCodeBlock ##여신계약기본 조회결과가 없습니다.
			// =============================================================================
			{

				throw new BizException("MZZZ00161", new String[] { "여신계약기본" },
						e);
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 세팅
		// =============================================================================
		String sLginOmNo = KContextUtil.getOptorEnob(); //#s로그인조직원번호
		String sLginOrzCd = KContextUtil.getBranchNumber(); //#s로그인조직코드

		String sOmNo = sLginOmNo; //#s조직원번호
		String sOrzCd = sLginOrzCd; //#s조직코드

		String sOmNm = sLginOmNo; //#s조직원명
		String sOrzNm = sLginOrzCd; //#s조직명

		String sExpDt = rLonCnrBasInq.getString("exp_dt"); //#s만기일자
		String sNowDt = KDateUtil.getCurrentDate("yyyyMMdd"); //#s현재일자

		String sEmalTxt = ""; //#s이메일내용

		//String [s오류코드] = "";
		//String [s오류내용] = "";
		//boolean [b오류여부] = false;

		long lEmalSndNcn = 0; //#l이메일발송건수
		long lSmsSndNcn = 0; //#lSMS발송건수
		if (KLDataConvertUtil.equals(iAamRpyJrsdOgDspcTsm.getString("in_dv"),
				"1")) {

			// =============================================================================
			// ######### CodeValidationBlock ##입력 검증2
			// =============================================================================
			{
				LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
				boolean existInvalidElement = false;

				//상품코드를 확인하십시오.
				if (KLDataConvertUtil.notEquals(
						KStringUtil.substring(
								iAamRpyJrsdOgDspcTsm.getString("ln_no"), 0, 3),
						"103")
						&& KLDataConvertUtil.notEquals(KStringUtil.substring(
								iAamRpyJrsdOgDspcTsm.getString("ln_no"), 0, 3),
								"303")) {
					bizExceptionMessage.setBizExceptionMessage("MZZZ00083",
							new String[] { "상품코드" });
					existInvalidElement = true;
				}

				//만기경과 대출건 입니다.
				if (sExpDt.compareTo(KDateUtil.addMonth(sNowDt, 1)) < 0) {
					bizExceptionMessage.setBizExceptionMessage("MLRL00434");
					existInvalidElement = true;
				}

				//위탁중으로 처리불가 합니다.
				if (KLDataConvertUtil.equals(rLonCnrBasInq.getString("csg_yn"),
						"Y")) {
					bizExceptionMessage.setBizExceptionMessage("MLRL00433",
							new String[] { "위탁중" });
					existInvalidElement = true;
				}

				//대출잔액을 확인하십시오.
				if (KLDataConvertUtil.equals(
						rLonCnrBasInq.getBigDecimal("ln_bal"), 0)) {
					bizExceptionMessage.setBizExceptionMessage("MZZZ00083",
							new String[] { "대출잔액" });
					existInvalidElement = true;
				}

				//기업금융팀, 부동산사업팀 처리불가
				if (KLDataConvertUtil.equals(
						rLonCnrBasInq.getString("jrsd_orz_cd"), "92783000")
						|| KLDataConvertUtil.equals(
								rLonCnrBasInq.getString("jrsd_orz_cd"),
								"92837000")) {

					bizExceptionMessage.setBizExceptionMessage("MLRL00435",
							new String[] { "소관기관" });
					existInvalidElement = true;
				}

				if (existInvalidElement) {
					bizExceptionMessage.throwBizException();
				}
			}
			if (KLDataConvertUtil.equals(KStringUtil.trimNisEmpty(sLginOmNo),
					false)) {

				// =============================================================================
				// ######### GeneralCodeBlock ##여신경리조직조회  입력값 세팅
				// =============================================================================
				iLonAcnyOrzInq = new LData();

				iLonAcnyOrzInq.setString("om_no", sOmNo);
				iLonAcnyOrzInq.setString("orz_cd", sOrzCd);

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_전액상환소관기관통보전송 - 여신경리조직조회  입력값 세팅");
					LLog.debug.println(iLonAcnyOrzInq);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iLonAcnyOrzInq);
				rLonAcnyOrzInq = lonAcnyOrzInqCpbc
						.retvLonAcnyOrz(iLonAcnyOrzInq); //##여신경리조직조회
				if (KLDataConvertUtil.equals(
						rLonAcnyOrzInq.getString("orz_cd"),
						rLonCnrBasInq.getString("jrsd_orz_cd"))) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##접수기관과 소관기관이 동일합니다.
					// =============================================================================
					{
						throw new BizException("MLRL00436", new String[] {
								"접수기관", "소관기관" });
					}
				} else if (KLDataConvertUtil.equals(
						rLonAcnyOrzInq.getString("orz_cd"), "91062000")
						|| KLDataConvertUtil.equals(
								rLonAcnyOrzInq.getString("orz_cd"), "92603000")
						|| KLDataConvertUtil.equals(
								rLonAcnyOrzInq.getString("orz_cd"), "91036000")) {

					// =============================================================================
					// ######### ExceptionCodeBlock ##본사소속사원으로 처리불가합니다.
					// =============================================================================
					{
						throw new BizException("MLRL00433",
								new String[] { "본사소속사원" });
					}
				} else {

					// =============================================================================
					// ######### GeneralCodeBlock ##전역변수 조직원정보입력 - 조직원명, 조직코드
					// =============================================================================
					sOrzCd = rLonAcnyOrzInq.getString("orz_cd");
					sOmNo = rLonAcnyOrzInq.getString("om_no");
					sOmNm = rLonAcnyOrzInq.getString("om_nm");
					sOrzNm = rLonAcnyOrzInq.getString("orz_nm");

					if (KStringUtil.trimNisEmpty(sOmNm)) {
						sOmNm = sLginOmNo;
					}
					if (KStringUtil.trimNisEmpty(sOrzNm)) {
						sOrzNm = sLginOrzCd;
					}
				}
			} else {

				// =============================================================================
				// ######### GeneralCodeBlock ##전역변수 조직원정보입력 - SYSTEM
				// =============================================================================
				sOmNo = "SYSTEM";
				sOmNm = "SYSTEM";
			}
			try {

				// =============================================================================
				// ######### GeneralCodeBlock ##상환문의관리[대출번호]목록조회  입력값 세팅
				// =============================================================================
				iRpyInqyMgtLnNoListInq.setString("ln_no",
						iAamRpyJrsdOgDspcTsm.getString("ln_no"));
				iRpyInqyMgtLnNoListInq.setLong("ln_seq",
						iAamRpyJrsdOgDspcTsm.getLong("ln_seq"));
				//[i상환문의관리대출번호목록조회].[전액상환문의일련번호]  = 
				iRpyInqyMgtLnNoListInq.setString("aam_rpy_inqy_ss_cd", "01");
				iRpyInqyMgtLnNoListInq.setString("aam_rpy_inqy_rcp_dt", sNowDt);
				iRpyInqyMgtLnNoListInq.setString("aam_rpy_inqy_cnl_yn", "N");

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_전액상환소관기관통보전송 - 상환문의관리lnNo목록조회  입력값 세팅");
					LLog.debug.println(iRpyInqyMgtLnNoListInq);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iRpyInqyMgtLnNoListInq);
				rRpyInqyMgtLnNoListInq = rpyInqyMgtHisEbc
						.retvLstRpyInqyMgtByLnNo(iRpyInqyMgtLnNoListInq); //##상환문의관리[대출번호]목록조회
			} catch (LBizException e) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##상환문의관리 조회 중 오류가 발생했습니다.
				// =============================================================================
				{

					throw new BizException("MBCM00193",
							new String[] { "상환문의관리" }, e);
				}
			}
			if (rRpyInqyMgtLnNoListInq.getDataCount() > 0) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##당일 안내이력이 있습니다.
				// =============================================================================
				{
					throw new BizException("MLRL00437", new String[] { "안내이력" });
				}
			}
		} else {

			// =============================================================================
			// ######### CodeValidationBlock ##입력검증 // 메일전송 버튼
			// =============================================================================
			{
				LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
				boolean existInvalidElement = false;

				if (KLDataConvertUtil.notEquals(
						rLonCnrBasInq.getString("lon_sale_chn_cd"), "01")) {

					bizExceptionMessage.setBizExceptionMessage("MLRL00450",
							new String[] { "전송" });
					existInvalidElement = true;
				}

				//if(([r여신계약기본조회].[여신상품코드] == "104006" && [r여신계약기본조회].[여신상품SCOPE번호] == "03") ||
				//    [r여신계약기본조회].[여신상품코드] == "104007" || 
				//    [r여신계약기본조회].[여신상품코드] == "104008" || 
				//    [r여신계약기본조회].[여신상품코드] == "104001" ||
				//    ([r여신계약기본조회].[여신상품코드] == "104022" && [r여신계약기본조회].[여신상품SCOPE번호] == "03") ||
				//    ([r여신계약기본조회].[여신상품코드] == "104025" && ([r여신계약기본조회].[여신상품SCOPE번호] == "01" || [r여신계약기본조회].[여신상품SCOPE번호] == "02"))
				//){
				//	code="MLRL00450";	@ 대상이 아닙니다.
				//	params="전송";
				//}

				if (existInvalidElement) {
					bizExceptionMessage.throwBizException();
				}
			}
		}
		try {

			// =============================================================================
			// ######### GeneralCodeBlock ##통합고객정보조회  입력값 세팅
			// =============================================================================
			iIgCsImInq.setString("ig_cs_no",
					rLonCnrBasInq.getString("ig_cs_no"));

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_전액상환소관기관통보전송 - 통합고객정보조회  입력값 세팅");
				LLog.debug.println(iIgCsImInq);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(iIgCsImInq);
			rIgCsImInq = csCommImIbc.retvIgCsIm(iIgCsImInq); //##통합고객정보조회
		} catch (LBizException e) {

			// =============================================================================
			// ######### ExceptionCodeBlock ##통합고객정보 조회 중 오류가 발생했습니다.
			// =============================================================================
			{

				throw new BizException("MBCM00193", new String[] { "통합고객정보" },
						e);
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##로컬변수 세팅 - 이메일내용
		// =============================================================================
		String sBlk = " "; //#s공백
		if (!(KStringUtil.trimNisEmpty(rIgCsImInq.getString("cs_nm")))) {
			sEmalTxt = iAamRpyJrsdOgDspcTsm.getString("ln_no") + sBlk
					+ rIgCsImInq.getString("cs_nm");
		} else {
			sEmalTxt = iAamRpyJrsdOgDspcTsm.getString("ln_no") + sBlk
					+ rLonCnrBasInq.getString("ig_cs_no");
		}
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_전액상환소관기관통보전송 - 로컬변수 세팅 - 이메일내용");
			LLog.debug.println(rIgCsImInq);
		}
		try {

			// =============================================================================
			// ######### GeneralCodeBlock ##여신제공조직원목록조회  입력값 세팅
			// =============================================================================
			String[] sHmreTpCd = new String[1]; //#s인사유형코드
			sHmreTpCd[0] = "117"; //내근직원

			iLonOfOmListInq.setString("dsms_yn", "1"); //위촉
			KCommMngUtil.setLDataToArrayList(iLonOfOmListInq, "hmre_tp_cd",
					sHmreTpCd);
			iLonOfOmListInq.setString("orz_cd",
					rLonCnrBasInq.getString("jrsd_orz_cd"));

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_전액상환소관기관통보전송 - 여신제공조직원목록조회  입력값 세팅");
				LLog.debug.println(iLonOfOmListInq);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(iLonOfOmListInq);
			iLonOfOmListInq.setInt("page_no",
					iAamRpyJrsdOgDspcTsm.getInt("page_no"));
			iLonOfOmListInq.setInt("page_size",
					iAamRpyJrsdOgDspcTsm.getInt("page_size"));
			rLonOfOmListInq = omBasEbc.retvLstLonOfOm(iLonOfOmListInq); //##여신제공조직원목록조회
			if (rLonOfOmListInq.getDataCount() > 0) {
				_total_row_cnt = LIndexPagingVO.getPagingVO(rLonOfOmListInq)
						.getTotalRowCnt();
				_total_page_cnt = LIndexPagingVO.getPagingVO(rLonOfOmListInq)
						.getTotalPageCnt();
			}
		} catch (LBizException e) {

			// =============================================================================
			// ######### ExceptionCodeBlock ##여신제공조직원목록 조회 중 오류가 발생했습니다.
			// =============================================================================
			{

				throw new BizException("MBCM00193",
						new String[] { "여신제공조직원목록조회" }, e);
			}
		}
		for (int inx = 0, inxLoopSize = rLonOfOmListInq.getDataCount(); inx < inxLoopSize; inx++) {
			LData tLonOfOmHis = rLonOfOmListInq.getLData(inx);
			LProtocolInitializeUtil.primitiveLMultiInitialize(tLonOfOmHis);

			// =============================================================================
			// ######### GeneralCodeBlock ##s수신자이메일주소 입력
			// =============================================================================
			if (KStringUtil.trimNisEmpty(sRcvrEmalAdr)) {
				sRcvrEmalAdr = tLonOfOmHis.getString("om_no");
			} else {
				sRcvrEmalAdr = sRcvrEmalAdr + ";"
						+ tLonOfOmHis.getString("om_no");
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##이메일발송건수 증가
			// =============================================================================
			lEmalSndNcn = lEmalSndNcn + 1;
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##로그출력 - 조직원조회결과
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_전액상환소관기관통보전송 - 로그출력 - 조직원조회결과");
			LLog.debug.println(rLonOfOmListInq);
			LLog.debug.println(sRcvrEmalAdr);
		}
		try {

			// =============================================================================
			// ######### GeneralCodeBlock ##이메일조직원발송등록  입력값 세팅
			// =============================================================================
			iEmalOmSndReg.setString("sndr_om_no", sOmNo);

			iEmalOmSndReg.setString("rcvr_om_no_txt", sRcvrEmalAdr);
			iEmalOmSndReg.setString("emal_titl_nm", "kbli전액상환문의가접수되었습니다.");
			iEmalOmSndReg.setString("ezmr_emal_txt_form_dv",
					EmalTxtFormDvCdConst.PLAINTEXT.getCode());
			iEmalOmSndReg.setString("emal_txt", sEmalTxt);

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_전액상환소관기관통보전송 - 이메일조직원발송등록  입력값 세팅");
				LLog.debug.println(iEmalOmSndReg);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(iEmalOmSndReg);
			igGeMgtIbc.regtEmalOmSnd(iEmalOmSndReg); //##이메일조직원발송등록
		} catch (LBizException e) {

			// =============================================================================
			// ######### GeneralCodeBlock ##오류로그출력
			// =============================================================================
			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_전액상환소관기관통보전송 - 오류로그출력");
				LLog.debug.println("■■■ 오류로그 >> "
						+ KExceptionUtil.getMessage(e));
			}

			// =============================================================================
			// ######### ExceptionCodeBlock ##이메일조직원발송 처리 중 오류가 발생하였습니다.
			// =============================================================================
			{

				throw new BizException("MZZZ00068",
						new String[] { "이메일조직원발송" }, e);
			}
		}
		try {
			rDtmGnoInq = lonCnrBasRcvPatEbc.retvDtmGno(); //##일시채번조회

			// =============================================================================
			// ######### GeneralCodeBlock ##상환문의관리내역등록  입력값 세팅
			// =============================================================================
			iRpyInqyMgtHisReg.setString("ln_no",
					iAamRpyJrsdOgDspcTsm.getString("ln_no"));
			iRpyInqyMgtHisReg.setLong("ln_seq",
					iAamRpyJrsdOgDspcTsm.getLong("ln_seq"));
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_seq",
					rDtmGnoInq.getString("dtm_gno_txt"));
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_ss_cd",
					iAamRpyJrsdOgDspcTsm.getString("aam_rpy_inqy_ss_cd"));
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_rcp_dt", sNowDt);

			if (KLDataConvertUtil.equals(sOmNo, "CM")) {
				iRpyInqyMgtHisReg.setString("aam_rpy_inqy_rcp_orz_cd",
						"91062000");
			} else {
				iRpyInqyMgtHisReg.setString("aam_rpy_inqy_rcp_orz_cd", sOrzCd);
			}
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_rcp_om_no", sOmNo);
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_csl_orz_cd", "");
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_csl_om_no", "");
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_cnl_yn", "N");
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_rsn_cd", "99"); //결과미등록
			iRpyInqyMgtHisReg.setString("aam_rpy_inqy_csl_txt", "");

			if (LLog.debug.isEnabled()) {
				LLog.debug
						.println("■■■ ACSD_전액상환소관기관통보전송 - 상환문의관리내역등록  입력값 세팅");
				LLog.debug.println(iRpyInqyMgtHisReg);
			}
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iRpyInqyMgtHisReg);
			rRpyInqyMgtHisReg = rpyInqyMgtHisEbc
					.regtRpyInqyMgtHis(iRpyInqyMgtHisReg); //##상환문의관리내역등록
		} catch (LBizException e) {

			// =============================================================================
			// ######### ExceptionCodeBlock ##상환문의관리내역등록 처리 중 오류가 발생하였습니다.
			// =============================================================================
			{

				throw new BizException("MZZZ00068",
						new String[] { "상환문의관리내역등록" }, e);
			}
		}
		if (KLDataConvertUtil.equals(
				iAamRpyJrsdOgDspcTsm.getString("sms_snd_yn"), "Y")) {

			// =============================================================================
			// ######### GeneralCodeBlock ##조직조직장정보조회  입력값 세팅
			// =============================================================================
			icOrzOrldImInq = new LData();
			icOrzOrldImInq.setString("orz_cd",
					rLonCnrBasInq.getString("jrsd_orz_cd"));

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_전액상환소관기관통보전송 - 조직조직장정보조회  입력값 세팅");
				LLog.debug.println(icOrzOrldImInq);
			}
			LProtocolInitializeUtil.primitiveLMultiInitialize(icOrzOrldImInq);
			rcOrzOrldImInq = hmreMgtIbc.retvOrzOrldIm(icOrzOrldImInq); //##조직조직장정보조회
			for (int inx = 0, inxLoopSize = rcOrzOrldImInq.getDataCount(); inx < inxLoopSize; inx++) {
				LData tOrzOrldImInqRsl = rcOrzOrldImInq.getLData(inx);
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(tOrzOrldImInqRsl);

				// =============================================================================
				// ######### GeneralCodeBlock ##고객연락처조회  입력값 세팅
				// =============================================================================
				iCsCtplInq = new LData();
				rCsCtplInq = new LData();
				iCsCtplInq.setString("om_no",
						tOrzOrldImInqRsl.getString("om_no"));
				iCsCtplInq.setString("inq_om_no", sOmNo);
				iCsCtplInq.setString("inq_orz_cd", sOrzCd);

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ ACSD_전액상환소관기관통보전송 - 고객연락처조회  입력값 세팅");
					LLog.debug.println(iCsCtplInq);
				}
				LProtocolInitializeUtil.primitiveLMultiInitialize(iCsCtplInq);
				rCsCtplInq = igGeMgtIbc.retvCsCtpl(iCsCtplInq); //##고객연락처조회

				// =============================================================================
				// ######### GeneralCodeBlock ##문자안내발송등록  입력값 세팅
				// =============================================================================
				// [i문자안내발송등록]  

				LData tSmsGeImDto = new LData(); //#tSMS안내정보Dto

				tSmsGeImDto.setString("ln_no",
						iAamRpyJrsdOgDspcTsm.getString("ln_no"));
				tSmsGeImDto.setString("cs_nm", rIgCsImInq.getString("cs_nm"));
				tSmsGeImDto.setString("prc_orz_nm", sOrzNm);
				tSmsGeImDto.setString("prc_om_nm", sOmNm);

				//[i문자안내발송등록].[통합안내추출그룹ID]             = 
				iChtrGeSndReg.setString("adn_no", "50055"); //전액상환안내시 상위결재자에게 알리미
				iChtrGeSndReg.setString("adn_crat_bde_dv_cd", "02"); //02:실시간
				//[i문자안내발송등록].[안내장생성일괄구분코드]         = [안내장생성일괄구분코드Const].[온라인];
				iChtrGeSndReg.setString("lob_idt_cnr_no1",
						iAamRpyJrsdOgDspcTsm.getString("ln_no"));
				iChtrGeSndReg.setString("lob_idt_cnr_no2", KTypeConverter
						.toString(iAamRpyJrsdOgDspcTsm.getLong("ln_seq")));
				//[i문자안내발송등록].[단체LOB식별계약번호]            = 

				//TODO 업무식별키는 확인필요
				//[i문자안내발송등록].[통합안내참조업무식별키구분코드] = 
				//[i문자안내발송등록].[통합안내참조업무식별키내용]     = 

				iChtrGeSndReg.setString("ig_cs_no",
						rLonCnrBasInq.getString("ig_cs_no"));
				iChtrGeSndReg.setString("ig_ge_cs_nm",
						rIgCsImInq.getString("cs_nm"));
				//[i문자안내발송등록].[발송예정일시]                   = KDateUtil.getDateTimeStr(); //일시취득 yyyyMMddHHmmss 14
				iChtrGeSndReg.setString("snd_trpn_dv_cd",
						SndTrpnDvCdConst.MEA_SVC_CHRG.getCode());
				iChtrGeSndReg.setString("vrf_ned_yn", "N");
				iChtrGeSndReg.setString("fm_mp_ned_yn", "Y");
				iChtrGeSndReg.setString("snd_rj_ex_yn", "N");
				//[i문자안내발송등록].[SMS발송대상구분코드]            = [안내발송대상구분코드Const].[발송대상];
				//[i문자안내발송등록].[LMS발송대상구분코드]            = 
				//[i문자안내발송등록].[MMS발송대상구분코드]            = 
				iChtrGeSndReg.setString("cco_snd_tgt_dv_cd",
						GeSndTgtDvCdConst.SND_TGT.getCode());
				//[i문자안내발송등록].[발송산출기준일자]               = KDateUtil.getDateStr();
				iChtrGeSndReg.setString("snd_req_om_no", sOmNo);
				iChtrGeSndReg.setString("snd_req_orz_cd", sOrzCd);
				iChtrGeSndReg.setString("sndr_rs_tlno",
						LonTlnoConst.FNN_CLCT.getCode());
				iChtrGeSndReg.setString("ge_snd_lms_titl_nm", "(교보생명)전액상환알림");
				iChtrGeSndReg.setString("jrsd_og_cd",
						rLonCnrBasInq.getString("jrsd_orz_cd"));

				iChtrGeSndReg.setString("ig_ge_mia_txt",
						BJsonConvertUtil.getJsonData(tSmsGeImDto));

				iChtrGeSndReg.setString("calg_tarno",
						rCsCtplInq.getString("calg_tarno"));
				iChtrGeSndReg.setString("calg_tgkno",
						rCsCtplInq.getString("calg_tgkno"));
				iChtrGeSndReg.setString("calg_tivno",
						rCsCtplInq.getString("calg_tivno"));

				if (LLog.debug.isEnabled()) {
					LLog.debug
							.println("■■■ APSD_320_여신예약이체내역등록 - 문자안내발송등록  입력값 세팅");
					LLog.debug.println(iChtrGeSndReg);
				}
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(iChtrGeSndReg);
				rChtrGeSndReg = igGeMgtIbc.regtChtrGeSnd(iChtrGeSndReg); //##문자안내발송등록
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##SMS발송건수 증가
			// =============================================================================
			lSmsSndNcn = lSmsSndNcn + 1;
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 입력
		// =============================================================================
		rAamRpyJrsdOgDspcTsm.setLong("emal_snd_ncn", lEmalSndNcn);
		rAamRpyJrsdOgDspcTsm.setLong("sms_snd_ncn", lSmsSndNcn);

		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_전액상환소관기관통보전송 - 결과값 입력");
			LLog.debug.println(rAamRpyJrsdOgDspcTsm);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rAamRpyJrsdOgDspcTsm);
		//#페이징 처리시 다음페이지 여부 
		rAamRpyJrsdOgDspcTsm.setInt("total_row_cnt", _total_row_cnt);
		rAamRpyJrsdOgDspcTsm.setInt("total_page_cnt", _total_page_cnt);
		return rAamRpyJrsdOgDspcTsm;
	}

	/**
	 * 주기변동금리선납제한검증
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055109
	 * @logicalName   주기변동금리선납제한검증
	 * @param LData iCycChgIttPpmtLmtVrf i주기변동금리선납제한검증
	 * @return LData rCycChgIttPpmtLmtVrf r주기변동금리선납제한검증
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_주기변동금리선납제한검증
	 * 
	 */
	public LData verifyCycChgIttPpmtLmt(LData iCycChgIttPpmtLmtVrf)
			throws LException {
		//#Return 변수 선언 및 초기화
		LData rCycChgIttPpmtLmtVrf = new LData(); //# r주기변동금리선납제한검증
													//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iIttChgOpHisLnNoListInq = new LData(); //# i금리변동작업내역대출번호목록조회
		LMultiData rIttChgOpHisLnNoListInq = new LMultiData(); //# r금리변동작업내역대출번호목록조회
		//#호출 컴포넌트 초기화
		IttChgOpHisEbc ittChgOpHisEbc = new IttChgOpHisEbc(); //# 금리변동작업내역Ebi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 세팅
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ 주기변동금리선납제한검증 START ");
			LLog.debug.println("■■■ 전역변수 SET ");
			LLog.debug.println(iCycChgIttPpmtLmtVrf);
		}

		String sOpDt = "00010101"; //#s작업일자
		String sOpBsDt = "00010101"; //#s작업기준일자
		String sNowDt = "00010101"; //#s현재일자
		sNowDt = KDateUtil.getCurrentDate("yyyyMMdd");

		//if([i주기변동금리선납제한검증].[주기변동금리선납제한검증여신계약기본입력Dto].[만기일자] > "00010101"){
		//	[s작업일자] = [i주기변동금리선납제한검증].[주기변동금리선납제한검증여신계약기본입력Dto].[만기일자];
		//}else{
		//	[s작업일자] = [i주기변동금리선납제한검증].[주기변동금리선납제한검증여신계약기본입력Dto].[최초만기일자];
		//}
		sOpDt = (((LData) iCycChgIttPpmtLmtVrf
				.get("cycChgIttPpmtLmtVrfLonCnrBasInDto")).getString("exp_dt"));

		//LLog.debug.println("■■■ 100_작업일자 : " + [s작업일자]);
		//LLog.debug.println("■■■ 100_현재일자 : " + [s현재일자]);

		// =============================================================================
		// ######### GeneralCodeBlock ##결과값 초기세팅
		// =============================================================================
		rCycChgIttPpmtLmtVrf.setString("bccl_chg_itt_bs_day_xc_dv", "0");
		rCycChgIttPpmtLmtVrf.setString("bccl_chg_itt_bs_dt", "00010101");
		if ((((LData) iCycChgIttPpmtLmtVrf.get("cycChgIttPpmtLmtVrfInDto"))
				.getString("rei_dt")).compareTo(sOpDt) >= 0) {

			// =============================================================================
			// ######### GeneralCodeBlock ##로그 출력
			// =============================================================================
			LLog.debug.println("■■■ 200_만기이후대상 SKIP ");

			// =============================================================================
			// ######### GeneralCodeBlock ##return
			// =============================================================================
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(rCycChgIttPpmtLmtVrf);
			return rCycChgIttPpmtLmtVrf;
		}
		if ((((LData) iCycChgIttPpmtLmtVrf
				.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
				.getLong("itt_chg_cyc_mcn")) > 0
				&& ((((LData) iCycChgIttPpmtLmtVrf
						.get("cycChgIttPpmtLmtVrfInDto"))
						.getBigDecimal("nml_irt"))
						.compareTo(new BigDecimal("0")) > 0 || (((LData) iCycChgIttPpmtLmtVrf
						.get("cycChgIttPpmtLmtVrfInDto"))
						.getBigDecimal("aa_irt"))
						.compareTo(new BigDecimal("0")) > 0)) {

			// =============================================================================
			// ######### GeneralCodeBlock ##금리변동작업내역[대출번호]목록조회  입력값 세팅
			// =============================================================================
			iIttChgOpHisLnNoListInq.setString("ln_no",
					(((LData) iCycChgIttPpmtLmtVrf
							.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
							.getString("ln_no")));
			iIttChgOpHisLnNoListInq.setLong("ln_seq",
					(((LData) iCycChgIttPpmtLmtVrf
							.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
							.getLong("ln_seq")));

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ 300_금리변동작업내역대출번호목록조회 입력값 SET ");
			}

			//LLog.debug.println("■■■ 300_대출번호     : " + [i금리변동작업내역대출번호목록조회].[대출번호]);
			//LLog.debug.println("■■■ 300_대출일련번호 : " + [i금리변동작업내역대출번호목록조회].[대출일련번호]);
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iIttChgOpHisLnNoListInq);
			rIttChgOpHisLnNoListInq = ittChgOpHisEbc
					.retvLstIttChgOpHisByLnNo(iIttChgOpHisLnNoListInq); //##금리변동작업내역[대출번호]목록조회

			// =============================================================================
			// ######### GeneralCodeBlock ##금리변동작업내역[대출번호]목록조회  결과값 맵핑
			// =============================================================================
			// 조회결과의 최근 1건만 Dto로 사용
			LData tIttChgOpHisDto = new LData(); //#t금리변동작업내역Dto

			if (rIttChgOpHisLnNoListInq.getDataCount() > 0) {
				tIttChgOpHisDto = rIttChgOpHisLnNoListInq.getLData(0);
			}
			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ 310_금리변동작업내역대출번호목록조회 결과값 SET ");
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##주기변동금리선납제한검증 로직
			// =============================================================================
			//LLog.debug.println("■■■ 320_t금리변동작업내역Dto                        : " + [t금리변동작업내역Dto]);
			//LLog.debug.println("■■■ 320_주기변동금리선납제한검증입력Dto             : " + [i주기변동금리선납제한검증].[주기변동금리선납제한검증입력Dto]);
			//LLog.debug.println("■■■ 320_주기변동금리선납제한검증여신계약기본입력Dto : " + [i주기변동금리선납제한검증].[주기변동금리선납제한검증여신계약기본입력Dto]);

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ 320_논리로직 START ");
			}

			if (rIttChgOpHisLnNoListInq.getDataCount() > 0
					&& (tIttChgOpHisDto.getString("nt_ch_dt"))
							.compareTo("00010101") > 0
					&& (tIttChgOpHisDto.getString("nt_ch_dt"))
							.compareTo((((LData) iCycChgIttPpmtLmtVrf
									.get("cycChgIttPpmtLmtVrfInDto"))
									.getString("cmpe_ls_dt"))) <= 0) {

				// 금리변경예정일 -1일
				sOpBsDt = KDateUtil.addDay(
						tIttChgOpHisDto.getString("nt_ch_dt"), -1);
				if (LLog.debug.isEnabled()) {
					LLog.debug.println("■■■ 320_작업기준일자 : " + sOpBsDt);
				}
				//03:원리금균등, 05:거치후원리금균등
				if (KLDataConvertUtil.equals((((LData) iCycChgIttPpmtLmtVrf
						.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
						.getString("rpy_mth_cd")), "03")
						|| KLDataConvertUtil
								.equals((((LData) iCycChgIttPpmtLmtVrf
										.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
										.getString("rpy_mth_cd")), "05")) {
					if ((((LData) iCycChgIttPpmtLmtVrf
							.get("cycChgIttPpmtLmtVrfInDto"))
							.getLong("insl_pa_tim")) > 0
							&& (((LData) iCycChgIttPpmtLmtVrf
									.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
									.getString("ln_dt")).compareTo("20090413") >= 0
							&& (((LData) iCycChgIttPpmtLmtVrf
									.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
									.getString("ln_dt")).compareTo("20091001") <= 0) {

						//예정일 이후 익 납입응당일 구하기
						String sTempOpDt1 = (((LData) iCycChgIttPpmtLmtVrf
								.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
								.getString("pa_fsr_dt")); //#s임시작업일자1
						if (LLog.debug.isEnabled()) {
							LLog.debug.println("■■■ 320_임시작업일자1 - 납입응당일자: "
									+ sTempOpDt1);
						}

						while (sTempOpDt1.compareTo(tIttChgOpHisDto
								.getString("nt_ch_dt")) < 0) {

							boolean bEmthDtYn = KDateUtil.isLastDay(sTempOpDt1); //#b월말일자여부
							// 1개월 증가
							sTempOpDt1 = KDateUtil.addMonth(sTempOpDt1, 1);

							// 말일자이면 말일자로 변경
							if (KLDataConvertUtil.equals(bEmthDtYn, true)) {
								sTempOpDt1 = KDateUtil.getLastDay(sTempOpDt1);
							}

						}
						if (LLog.debug.isEnabled()) {
							LLog.debug
									.println("■■■ 320_임시작업일자1 - 차기변경일 이후로 변경 후 : "
											+ sTempOpDt1);
						}

						// 금리변경예정일 이후 익 응당일 -1일
						String sTempOpDt2 = KDateUtil.addDay(sTempOpDt1, -1); //#s임시작업일자2
						if (LLog.debug.isEnabled()) {
							LLog.debug
									.println("■■■ 320_임시작업일자2 - 금리변경예정일 이후 익 응당일 -1일 : "
											+ sTempOpDt2);
						}

						if ((((LData) iCycChgIttPpmtLmtVrf
								.get("cycChgIttPpmtLmtVrfInDto"))
								.getString("cmpe_ls_dt")).compareTo(sTempOpDt2) > 0) {
							if (KLDataConvertUtil
									.equals((((LData) iCycChgIttPpmtLmtVrf
											.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
											.getString("rpy_mth_cd")), "05")) {
								//할부구간체크
								boolean bEmthDtYn = KDateUtil
										.isLastDay((((LData) iCycChgIttPpmtLmtVrf
												.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
												.getString("nt_rpy_dt"))); //#b월말일자여부
								String sTempOpDt3 = KDateUtil
										.addMonth(
												(((LData) iCycChgIttPpmtLmtVrf
														.get("cycChgIttPpmtLmtVrfLonCnrBasInDto"))
														.getString("nt_rpy_dt")),
												-1); //#s임시작업일자3

								if (LLog.debug.isEnabled()) {
									LLog.debug
											.println("■■■ 320_임시작업일자3 - 차기상환일자 -1 일 : "
													+ sTempOpDt3);
								}

								// 말일자이면 말일자로 변경
								if (KLDataConvertUtil.equals(bEmthDtYn, true)) {
									sTempOpDt3 = KDateUtil
											.getLastDay(sTempOpDt3);

									if (LLog.debug.isEnabled()) {
										LLog.debug
												.println("■■■ 320_임시작업일자3 말일자로 변경 : "
														+ sTempOpDt3);
									}
								}

								//선납불가 (할부구간이 아님 - 금리변경예정일기준)
								if (sOpBsDt.compareTo(sTempOpDt3) < 0) {
									rCycChgIttPpmtLmtVrf.setString(
											"bccl_chg_itt_bs_day_xc_dv", "1");
									//선납불가
								} else {
									rCycChgIttPpmtLmtVrf.setString(
											"bccl_chg_itt_bs_day_xc_dv", "1");
									sOpBsDt = sTempOpDt2;
								}
								//선납불가
							} else {
								rCycChgIttPpmtLmtVrf.setString(
										"bccl_chg_itt_bs_day_xc_dv", "1");
								sOpBsDt = sTempOpDt2;
							}
						}
					} else {
						rCycChgIttPpmtLmtVrf.setString(
								"bccl_chg_itt_bs_day_xc_dv", "1");
					}
				} else {
					rCycChgIttPpmtLmtVrf.setString("bccl_chg_itt_bs_day_xc_dv",
							"1");
				}
			}
			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_주기변동금리선납제한검증 - 주기변동금리선납제한검증 로직");
				LLog.debug.println(rCycChgIttPpmtLmtVrf);

			}

			//LLog.debug.println("■■■ 320_논리로직 END ");
			//LLog.debug.println("■■■ 320_작업기준일자 : " + [s작업기준일자]);
			//LLog.debug.println("■■■ 320_주기별변동금리기준일초과구분     : " + [r주기변동금리선납제한검증].[주기별변동금리기준일초과구분]);
		}
		if (KLDataConvertUtil.equals(
				rCycChgIttPpmtLmtVrf.getString("bccl_chg_itt_bs_day_xc_dv"),
				"1") && sOpBsDt.compareTo("00010101") > 0) {

			// =============================================================================
			// ######### GeneralCodeBlock ##결과값 입력 - 주기별변동금리기준일자
			// =============================================================================
			rCycChgIttPpmtLmtVrf.setString("bccl_chg_itt_bs_dt", sOpBsDt);
			if (sNowDt.compareTo((((LData) iCycChgIttPpmtLmtVrf
					.get("cycChgIttPpmtLmtVrfInDto")).getString("rei_dt"))) >= 0) {

				// =============================================================================
				// ######### ExceptionCodeBlock ##오류처리 : 주기별 변동금리 건으로 (@) 이후로는 선납이 불가합니다.
				// =============================================================================
				{
					throw new BizException("MLRL00341",
							new String[] { rCycChgIttPpmtLmtVrf
									.getString("bccl_chg_itt_bs_dt") });
				}
			} else {

				// =============================================================================
				// ######### GeneralCodeBlock ##결과값 입력 - 주기별변동금리기준일초과구분 2
				// =============================================================================
				rCycChgIttPpmtLmtVrf
						.setString("bccl_chg_itt_bs_day_xc_dv", "2");
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##종료로그 출력
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_주기변동금리선납제한검증 - 종료로그 출력");
			LLog.debug.println(rCycChgIttPpmtLmtVrf);
		}
		LProtocolInitializeUtil.primitiveLMultiInitialize(rCycChgIttPpmtLmtVrf);
		return rCycChgIttPpmtLmtVrf;
	}

	/**
	 * 청산대상미결잔액조회
	 *
	 * @designSeq     
	 * @serviceID     ZLRL055110
	 * @logicalName   청산대상미결잔액조회
	 * @param LData iLqdTgtInatBalInq i청산대상미결잔액조회
	 * @return LData rLqdTgtInatBalInq r청산대상미결잔액조회
	 * @exception     LException occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::04.공통프로세스컴포넌트::수납관리::원리금수납공통Cpbi::CORA_원리금수납공통Cpbi::ACSD_청산대상미결잔액조회
	 * 
	 */
	public LData retvLqdTgtInatBal(LData iLqdTgtInatBalInq) throws LException {
		//#Return 변수 선언 및 초기화
		LData rLqdTgtInatBalInq = new LData(); //# r청산대상미결잔액조회
		rLqdTgtInatBalInq.set("lqdTgtInatBalInqRslDto", new LData()); //# r청산대상미결잔액조회.청산대상미결잔액조회결과Dto
		rLqdTgtInatBalInq.set("pcitClcInatLqdDto", new LMultiData()); //# r청산대상미결잔액조회.원리금계산미결청산Dto
		rLqdTgtInatBalInq.set("pcitClcScDto", new LMultiData()); //# r청산대상미결잔액조회.원리금계산구간Dto
		rLqdTgtInatBalInq.set("pcitAfrvRslDto", new LData()); //# r청산대상미결잔액조회.원리금수납후결과Dto
																//#호출 오퍼레이션에서 사용된 파라미터 초기화
		LData iLonInatOccHisInatLqdListInq = new LData(); //# i여신미결발생내역미결청산목록조회
		LMultiData rLonInatOccHisInatLqdListInq = new LMultiData(); //# r여신미결발생내역미결청산목록조회
		//#호출 컴포넌트 초기화
		LonInatOccHisEbc lonInatOccHisEbc = new LonInatOccHisEbc(); //# 여신미결발생내역Ebi

		// =============================================================================
		// ######### GeneralCodeBlock ##전역변수 설정
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ 청산대상미결잔액조회Cpbi START");
			LLog.debug.println(iLqdTgtInatBalInq);
		}

		BigDecimal bdTotInatBal = new BigDecimal("0"); //#bd합계미결잔액
		BigDecimal bdSsrLqdAmt = new BigDecimal("0"); //#bd가수청산금액
		BigDecimal bdUncLqdAmt = new BigDecimal("0"); //#bd미수청산금액
		BigDecimal bdIadtLqdAmt = new BigDecimal("0"); //#bd가급청산금액
		BigDecimal bdTotReiAmt = new BigDecimal("0"); //#bd합계영수금액

		if (KStringUtil.trimNisEmpty((((LData) iLqdTgtInatBalInq
				.get("lqdTgtInatBalInqInDto")).getString("oplt_yn")))) {
			((LData) iLqdTgtInatBalInq.get("lqdTgtInatBalInqInDto")).setString(
					"oplt_yn", "");
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##여신미결발생내역[미결청산]목록조회  입력값 세팅
		// =============================================================================
		/*
		대출번호
		취소여부
		미결잔액 > 입력.미결잔액

		 */

		iLonInatOccHisInatLqdListInq.setString("ln_no",
				(((LData) iLqdTgtInatBalInq.get("lqdTgtInatBalInqInDto"))
						.getString("ln_no")));
		iLonInatOccHisInatLqdListInq.setBigDecimal("inat_bal",
				(((LData) iLqdTgtInatBalInq.get("lqdTgtInatBalInqInDto"))
						.getBigDecimal("inat_bal")));
		iLonInatOccHisInatLqdListInq.setString("cnl_yn",
				(((LData) iLqdTgtInatBalInq.get("lqdTgtInatBalInqInDto"))
						.getString("cnl_yn")));

		//결과값 초기화
		rLqdTgtInatBalInq.set("lqdTgtInatBalInqRslDto", new LData());
		rLqdTgtInatBalInq.set("pcitClcInatLqdDto", new LMultiData());
		LProtocolInitializeUtil
				.primitiveLMultiInitialize(iLonInatOccHisInatLqdListInq);
		rLonInatOccHisInatLqdListInq = lonInatOccHisEbc
				.retvLstLonInatOccHisByInatLqd(iLonInatOccHisInatLqdListInq); //##여신미결발생내역[미결청산]목록조회
		if (KLDataConvertUtil.equals((((LData) iLqdTgtInatBalInq
				.get("lqdTgtInatBalInqInDto")).getString("in_dv")), "1") //조회
		) {
			for (int inx = 0, inxLoopSize = rLonInatOccHisInatLqdListInq
					.getDataCount(); inx < inxLoopSize; inx++) {
				LData tLonInatOccHisInatLqdList = rLonInatOccHisInatLqdListInq
						.getLData(inx);
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(tLonInatOccHisInatLqdList);

				// =============================================================================
				// ######### GeneralCodeBlock ##합계미결잔액 계산
				// =============================================================================
				//합계미결잔액
				if (KLDataConvertUtil
						.equals(tLonInatOccHisInatLqdList
								.getString("inat_dv_cd"), "10")) { //가수금
					bdTotInatBal = bdTotInatBal
							.subtract(tLonInatOccHisInatLqdList
									.getBigDecimal("inat_bal"));

					if (KLDataConvertUtil.equals(
							(((LData) iLqdTgtInatBalInq
									.get("lqdTgtInatBalInqInDto"))
									.getString("oplt_yn")), "Y")) {
						((LData) rLqdTgtInatBalInq
								.get("lqdTgtInatBalInqRslDto")).setString(
								"rei_dt", tLonInatOccHisInatLqdList
										.getString("inat_occ_dt"));
					}
				} else {
					bdTotInatBal = bdTotInatBal.add(tLonInatOccHisInatLqdList
							.getBigDecimal("inat_bal"));
				}
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##결과 합계미결잔액 세팅
			// =============================================================================
			((LData) rLqdTgtInatBalInq.get("lqdTgtInatBalInqRslDto"))
					.setBigDecimal("tot_inat_bal", bdTotInatBal);

			//LLog.debug.println("■■■ 청산대상미결잔액조회Cpbi 합계미결잔액 : " + [r청산대상미결잔액조회].[청산대상미결잔액조회결과Dto].[합계미결잔액]);
			//LLog.debug.println("■■■ 청산대상미결잔액조회Cpbi END");
		} else if (KLDataConvertUtil.equals((((LData) iLqdTgtInatBalInq
				.get("lqdTgtInatBalInqInDto")).getString("in_dv")), "2") //계산
		) {
			for (int jnx = 0, jnxLoopSize = rLonInatOccHisInatLqdListInq
					.getDataCount(); jnx < jnxLoopSize; jnx++) {
				LData tLonInatOccHisInatLqdList = rLonInatOccHisInatLqdListInq
						.getLData(jnx);
				LProtocolInitializeUtil
						.primitiveLMultiInitialize(tLonInatOccHisInatLqdList);

				// =============================================================================
				// ######### GeneralCodeBlock ##결과값 목록 세팅
				// =============================================================================
				//LIST SET
				LData tPcitClcInatLqdDto = new LData(); //#t원리금계산미결청산Dto
				LData tPcitClcScDto = new LData(); //#t원리금계산구간Dto

				//미결발생목록
				// tPcitClcInatLqdDto <- tLonInatOccHisInatLqdList;
				tPcitClcInatLqdDto.setString("inat_occ_seq",
						tLonInatOccHisInatLqdList.getString("inat_occ_seq")); //#미결발생일련번호
				tPcitClcInatLqdDto.setString("inat_dv_cd",
						tLonInatOccHisInatLqdList.getString("inat_dv_cd")); //#미결구분코드
				tPcitClcInatLqdDto.setString("inat_dtl_cd",
						tLonInatOccHisInatLqdList.getString("inat_dtl_cd")); //#미결상세코드
				tPcitClcInatLqdDto.setBigDecimal("inat_bal",
						tLonInatOccHisInatLqdList.getBigDecimal("inat_bal")); //#미결잔액
				tPcitClcInatLqdDto.setString("inat_occ_dt",
						tLonInatOccHisInatLqdList.getString("inat_occ_dt")); //#미결발생일자
				((LMultiData) rLqdTgtInatBalInq.get("pcitClcInatLqdDto"))
						.addLData(tPcitClcInatLqdDto);

				if (KLDataConvertUtil
						.equals(tLonInatOccHisInatLqdList
								.getString("inat_dv_cd"), "10")) { //가수금

					tPcitClcScDto.setString("pcit_rcv_clc_dv_cd", "602");
					tPcitClcScDto.setString("pcit_rcv_clc_dv_cd_nm", "가수청산금액");
					tPcitClcScDto.setBigDecimal("amt", new BigDecimal("-1")
							.multiply(tLonInatOccHisInatLqdList
									.getBigDecimal("inat_bal")));
					//	[t원리금수납후결과Dto].[가수청산금액] = [t원리금수납후결과Dto].[가수청산금액] + [t원리금계산구간Dto].[금액];
					bdSsrLqdAmt = bdSsrLqdAmt.add(tPcitClcScDto
							.getBigDecimal("amt"));

				} else if (KLDataConvertUtil
						.equals(tLonInatOccHisInatLqdList
								.getString("inat_dv_cd"), "20")) { //미수금

					tPcitClcScDto.setString("pcit_rcv_clc_dv_cd", "604");
					tPcitClcScDto.setString("pcit_rcv_clc_dv_cd_nm", "미수청산금액");
					tPcitClcScDto
							.setBigDecimal("amt", tLonInatOccHisInatLqdList
									.getBigDecimal("inat_bal"));
					//	[t원리금수납후결과Dto].[미수청산금액] = [t원리금수납후결과Dto].[미수청산금액] + [t원리금계산구간Dto].[금액];
					bdUncLqdAmt = bdUncLqdAmt.add(tPcitClcScDto
							.getBigDecimal("amt"));

				} else {

					tPcitClcScDto.setString("pcit_rcv_clc_dv_cd", "605");
					tPcitClcScDto.setString("pcit_rcv_clc_dv_cd_nm", "가급청산금액");
					tPcitClcScDto
							.setBigDecimal("amt", tLonInatOccHisInatLqdList
									.getBigDecimal("inat_bal"));
					//	[t원리금수납후결과Dto].[가급청산금액] = [t원리금수납후결과Dto].[가급청산금액] + [t원리금계산구간Dto].[금액];
					bdIadtLqdAmt = bdIadtLqdAmt.add(tPcitClcScDto
							.getBigDecimal("amt"));

					if (KLDataConvertUtil.notEquals(
							tLonInatOccHisInatLqdList.getString("inat_dtl_cd"),
							"19")
							&& KLDataConvertUtil.notEquals(
									tLonInatOccHisInatLqdList
											.getString("inat_dtl_cd"), "20")) {
						//TODO LCDZ24MC1 INPUT 셋팅 하지만 LCDZ24MC 를 부르지 않아보여서 일단 PASS
						//		ii++;

					}
				}

				tPcitClcScDto.setBigDecimal("rckg_amt", new BigDecimal("0"));
				tPcitClcScDto.setString("clc_st_dt", "00010101");
				tPcitClcScDto.setString("clc_ed_dt", "00010101");
				tPcitClcScDto.setBigDecimal("ap_itt", new BigDecimal("0"));
				tPcitClcScDto.setLong("dcn", 0);
				tPcitClcScDto.setLong("clc_ncn", 1);
				bdTotReiAmt = bdTotReiAmt.add(tPcitClcScDto
						.getBigDecimal("amt"));

				//MC1 = [t여신미결발생내역미결청산목록]
				//MC2 = [t원리금수납후결과Dto]
				//MC3 = [t원리금계산구간Dto]

				//원리금계산구간
				((LMultiData) rLqdTgtInatBalInq.get("pcitClcScDto"))
						.addLData(tPcitClcScDto);
			}

			// =============================================================================
			// ######### GeneralCodeBlock ##결과값 세팅
			// =============================================================================
			//LLog.debug.println("■■■ 청산대상미결잔액조회_계산_OUTPUT SET");
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"ssr_lqd_amt", bdSsrLqdAmt);
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"unc_lqd_amt", bdUncLqdAmt);
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"iadt_lqd_amt", bdIadtLqdAmt);
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"tot_rei_amt", bdTotReiAmt);

			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setString(
					"nt_cmpe_ls_dt", (((LData) iLqdTgtInatBalInq
							.get("lonCnrBasDtotb")).getString("cmpe_ls_dt")));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setString(
					"nt_pa_fsr_dt", (((LData) iLqdTgtInatBalInq
							.get("lonCnrBasDtotb")).getString("pa_fsr_dt")));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setString(
					"nt_rpy_dt", (((LData) iLqdTgtInatBalInq
							.get("lonCnrBasDtotb")).getString("nt_rpy_dt")));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"nt_rpy_amt",
					(((LData) iLqdTgtInatBalInq.get("lonCnrBasDtotb"))
							.getBigDecimal("nt_rpy_amt")));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"nt_insl_amt", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto"))
					.setLong(
							"rmd_tim",
							(((LData) iLqdTgtInatBalInq.get("lonCnrBasDtotb"))
									.getLong("insl_ttim"))
									- (((LData) iLqdTgtInatBalInq
											.get("lonCnrBasDtotb"))
											.getLong("insl_pa_tim")));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setLong(
					"insl_pa_tim", (((LData) iLqdTgtInatBalInq
							.get("lonCnrBasDtotb")).getLong("insl_pa_tim")));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"ln_bal",
					(((LData) iLqdTgtInatBalInq.get("lonCnrBasDtotb"))
							.getBigDecimal("ln_bal")));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setLong(
					"ppmt_occ_dcn", 0);
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setLong(
					"ppmt_ept_dcn", 0);
			//공면일수없음
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"nml_irt", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"aa_irt", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"irt_pst_rpy_amt", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"adpt_cotx", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setString(
					"adpt_cotx_py_dv_cd", "N");
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"erpd_rpy_fee", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"etc_amt", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setString(
					"irt_st_dt", "00010101");
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setString(
					"irt_ed_dt", "00010101");
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"fxtm_rpy_amt", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"rm_rpy_amt", new BigDecimal("0"));
			((LData) rLqdTgtInatBalInq.get("pcitAfrvRslDto")).setBigDecimal(
					"insl_pcp", new BigDecimal("0"));

			if (LLog.debug.isEnabled()) {
				LLog.debug.println("■■■ ACSD_청산대상미결잔액조회 - 결과값 세팅");
				LLog.debug.println(rLqdTgtInatBalInq);
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##결과로그
		// =============================================================================
		if (LLog.debug.isEnabled()) {
			LLog.debug.println("■■■ ACSD_청산대상미결잔액조회 - 결과로그");
			LLog.debug.println(rLqdTgtInatBalInq);
		}
		LProtocolInitializeUtil.LdataInitialize(rLqdTgtInatBalInq);
		return rLqdTgtInatBalInq;
	}
}