/* --------------------------------------------------------------------------------------
 * Name : AaRegManual.JAVA 
 * VER  : 1.0
 * PROJ : 교보생명 V3 Project
 * Copyright 교보생명보험주식회사 rights reserved.
 * 
 */
package kv3.batch.lon.rl.job.aamgt.aaregjob;

import kv3.batch.core.component.worker.KAbstractCustomWorker;
import kv3.batch.core.component.worker.KAbstractIterableWorker;
import kv3.util.KBatchUtil;
import kv3.util.KDBCryptUtil;
import devon.scp.batch.core.exec.JobContext;
import devon.scp.batch.core.exception.worker.BatchSkipException;
import devon.scp.batch.core.exception.worker.BatchSkipAndRollbackException;
import kv3.batch.core.exception.KBatchExecuteSkipException;
import devon.scp.batch.core.component.reader.AbstractReader;
import devon.scp.batch.core.component.reader.ReaderIF;
import devon.scp.batch.core.component.ItemCurrentStateContants.STATE;
import devon.scp.batch.core.persistence.bulk.read.SplitDBReader;
import kv3.batch.core.component.reader.impl.KRawBufferedFileReader;
import devon.scp.batch.core.persistence.bulk.writer.LBatchUpdateDao;
import devon.scp.batch.core.persistence.bulk.writer.LBatchUpdateDaoCreator;

import kv3.batch.service.online.KOnlineServiceCaller;
import kv3.batch.service.inf.KInterfaceServiceCaller;
import kv3.batch.service.inf.KInterfaceInitContext;

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
import kv3.zzz.constants.BchCntcPrcOrzImConst;

import kv3.util.KDateUtil;
import kv3.util.KStringUtil;
import kv3.util.KBizDateUtil;
import kv3.util.KCommMngUtil;
import devon.core.log.LLog;
import kv3.util.KExceptionUtil;
import kv3.util.KTypeConverter;
import kv3.util.KIntegrationCodeUtil;
import kv3.util.KInterfaceDataConvertUtil;
import kv3.zzz.cpbc.bchopmgt.bchopmgtcommcpbi.BchOpMgtCommCpbc;
import kv3.lon.rl.cpbc.rcvmgt.pcitrcvcommcpbi.PcitRcvCommCpbc;
import kv3.common.ibc.cci.IgCsCntcImIbc;
import kv3.batch.util.KOndemandBatchUtil;

/**
 * 
 *
 * @logicalName   연체등록Manual
 * @version       1.0, 2023-11-15
 * @modelVersion  
 * @modelProject  KV3_MDL_LON_RL(소매여신)
 * @fullPath      2.시스템명세모델::06.배치::연체관리::연체등록Job::연체등록Manual
 * @jobId  		 BLRL250063
 * @stepId		  BLRL25006301
 */
public class AaRegManual extends KAbstractCustomWorker {
	/**
	 * 
	 * 
	 * @logicalName   mt기준일자
	 */
	private String mtBsDt = "";
	/**
	 * 
	 * 
	 * @logicalName   mt실제연체계산기준일자
	 */
	private String mtRealAaClcBsDt = "";
	/**
	 * 
	 * 
	 * @logicalName   mt연체계산기준일자
	 */
	private String mtAaClcBsDt = "";
	/**
	 * 
	 * 
	 * @logicalName   mt휴일여부
	 */
	private Boolean mtHldyYn;

	/**
	 * 
	 *
	 * @logicalName   선실행
	 * @param JobContext context 
	 * @return void 
	 * @exception     Exception occurs error
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      
	 * 
	 */
	public void beforeExecute(JobContext context) throws Exception {

	}

	/**
	 * 
	 *
	 * @logicalName   실행
	 * @return  
	 * @exception     Exception occurs error 
	 * @modelProject  KV3_MDL_LON_RL(소매여신)
	 * @fullPath      2.시스템명세모델::06.배치::연체관리::연체등록Job::CORA_연체등록Manual::ACSD_연체등록Manual_실행
	 * 
	 */
	public Object execute(JobContext context) throws Exception {
		//Initalize parameter used in calling Operation
		LData iBchOpExecHisStReg = new LData(); //# i배치작업실행내역시작등록
		String iAffcAaBasAaReg = ""; //# i사후연체기본연체등록
		LMultiData rAaRegTgtMeaSpcBdUncIrtListInq = new LMultiData(); //# r연체등록대상유지특수채권미수이자목록조회
		LData iAaRckgDayCu = new LData(); //# i연체기산일산출
		LData rAaRckgDayCu = new LData(); //# r연체기산일산출
		LData iRttOrzOlnDetsInq = new LData(); //# i퇴직조직원대출명세조회
		LData rRttOrzOlnDetsInq = new LData(); //# r퇴직조직원대출명세조회
		LData iEismCrdLnSbcRemRecpDtInq = new LData(); //# i임직원신용대출가입입금수령일자조회
		LData rEismCrdLnSbcRemRecpDtInq = new LData(); //# r임직원신용대출가입입금수령일자조회
		LData iEsopCrdLnTrmDtInq = new LData(); //# i우리사주신용대출해지일자조회
		LData rEsopCrdLnTrmDtInq = new LData(); //# r우리사주신용대출해지일자조회
		LData iAutTrsRqHisInq = new LData(); //# i자동이체청구내역조회
		LData rAutTrsRqHisInq = new LData(); //# r자동이체청구내역조회
		LData iAffcAaBasAaSsInq = new LData(); //# i사후연체기본연체상태조회
		LData rAffcAaBasAaSsInq = new LData(); //# r사후연체기본연체상태조회
		LData iSpcBdBasInq = new LData(); //# i특수채권기본조회
		LData rSpcBdBasInq = new LData(); //# r특수채권기본조회
		LData iLonInatOccHisInq = new LData(); //# i여신미결발생내역조회
		LData rLonInatOccHisInq = new LData(); //# r여신미결발생내역조회
		LData iAffcAaBasAaRelsSsUpd = new LData(); //# i사후연체기본연체해제상태수정
		long rAffcAaBasAaRelsSsUpd = 0; //# r사후연체기본연체해제상태수정
		LData icCsCntcHisReg = new LData(); //# ic고객접촉내역등록
		LData rcCsCntcHisReg = new LData(); //# rc고객접촉내역등록
		LData iAffcAaBasAaSsUpd = new LData(); //# i사후연체기본연체상태수정
		long rAffcAaBasAaSsUpd = 0; //# r사후연체기본연체상태수정
		LData iAffcAaBasNwAaReg = new LData(); //# i사후연체기본신규연체등록
		long rAffcAaBasNwAaReg = 0; //# r사후연체기본신규연체등록
		LData iAffcAaBasAccuAaDcnUpd = new LData(); //# i사후연체기본누적연체일수수정
		long rAffcAaBasAccuAaDcnUpd = 0; //# r사후연체기본누적연체일수수정
		LData iBchOpExecHisEdUpd = new LData(); //# i배치작업실행내역종료수정
		//#호출 컴포넌트 초기화
		BchOpMgtCommCpbc bchOpMgtCommCpbc = new BchOpMgtCommCpbc();
		PcitRcvCommCpbc pcitRcvCommCpbc = new PcitRcvCommCpbc();
		IgCsCntcImIbc igCsCntcImIbc = new IgCsCntcImIbc();

		LCommonDao commonDao;

		// =============================================================================
		// ######### CodeValidationBlock ##입력 파라미터 확인
		// =============================================================================
		{
			LBizExceptionMessage bizExceptionMessage = new LBizExceptionMessage();
			boolean existInvalidElement = false;

			if (KStringUtil.isEmpty(getStoreData("bsDt"))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00075",
						new String[] { "기준일자" });
				existInvalidElement = true;
			}
			if (!(KDateUtil.isDate(getStoreData("bsDt")))) {
				bizExceptionMessage.setBizExceptionMessage("MZZZ00194",
						new String[] { "기준일자" });
				existInvalidElement = true;
			}

			if (existInvalidElement) {
				bizExceptionMessage.throwBizException();
			}
		}
		try {

			// =============================================================================
			// ######### GeneralCodeBlock ##배치작업실행내역시작등록  입력값 세팅
			// =============================================================================
			// [i배치작업실행내역시작등록]  
			iBchOpExecHisStReg = new LData();
			iBchOpExecHisStReg.setString("bch_op_id", context.getJobId());
			iBchOpExecHisStReg.setString("jflw_id", context.getJobId() + "01");
			iBchOpExecHisStReg.setString("op_bs_dt", getStoreData("bsDt"));
			KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iBchOpExecHisStReg);
			bchOpMgtCommCpbc.regtBchOpExecHisSt(iBchOpExecHisStReg); //##배치작업실행내역시작등록

			// =============================================================================
			// ######### GeneralCodeBlock ##commit
			// =============================================================================
			this.txCommit();
		} catch (Exception e) {
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##연체등록에 사용되는 기준일자 생성 (기준일자, 연체계산기준일자, 실연체기준일자, 영업일여부) 
		// =============================================================================
		mtBsDt = getStoreData("bsDt"); // 배치 PARA로 입력 받은 값
		mtAaClcBsDt = KDateUtil.addDay(getStoreData("bsDt"), 1); // 기준일자 + 1일
		mtRealAaClcBsDt = KDateUtil.addDay(
				KBizDateUtil.getBizDate(getStoreData("bsDt"), 0), 1); // 기준일자의 영업일 + 1일
		mtHldyYn = KBizDateUtil.isHoliday(getStoreData("bsDt")); // 휴일여부

		//KBizDateUtil.getBizDateNextHoliday("20470214");

		// =============================================================================
		// ######### GeneralCodeBlock ##연체등록 처리결과에 대한 건수 초기화 (전체건수, 연체대상건수, 연체처리건수, 생략건수, 자동이체청구건수, MBS매각대상건수, 특수채권건수, 미수이자대상건수, 오류건수, 비영업일처리건수, 신규연체건수, 현재연체건수, 현채연체해제건수)
		// =============================================================================
		LData tAaRegPrcNcnMgtDto = new LData(); //#t연체등록처리건수관리Dto
		tAaRegPrcNcnMgtDto = KCommMngUtil.initLData(tAaRegPrcNcnMgtDto,
				new String[] { "all_ncn", "aa_tgt_ncn", "aa_prc_ncn",
						"omss_ncn", "aut_trs_rq_ncn", "mbs_sel_tgt_ncn",
						"spc_bd_ncn", "unc_irt_tgt_ncn", "err_ncn",
						"nbz_day_prc_ncn", "nw_aa_ncn", "now_aa_ncn",
						"now_aa_rels_ncn" }, new String[] { "LONG", "LONG",
						"LONG", "LONG", "LONG", "LONG", "BIGDECIMAL", "LONG",
						"LONG", "LONG", "LONG", "LONG", "LONG" }, true);

		// =============================================================================
		// ######### GeneralCodeBlock ##write  입력값 세팅 (배치 실행정보 출력)
		// =============================================================================
		// [i사후연체기본연체등록]  

		String sMg = "==================================================================================================================\n"
				+ "JOB NAME : <연체등록> \n"
				+ "JOB ID   : <BLRL250063> \n"
				+ "기준일자             : "
				+ mtBsDt
				+ "\n"
				+ "연체계산기준일자     : "
				+ mtAaClcBsDt
				+ "\n"
				+ "실제연체계산기준일자 : "
				+ mtRealAaClcBsDt
				+ "\n"
				+ "========================================================================================연체등록 START=========>>> \n\n"; //#s메시지

		iAffcAaBasAaReg = sMg;
		writer.writeBytes(iAffcAaBasAaReg);
		commonDao = new LCommonDao(
				"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvLstAaRegTgtByMea",
				null); //##연체등록대상[유지][특수채권][미수이자]목록조회
		rAaRegTgtMeaSpcBdUncIrtListInq = commonDao.executeQuery();
		for (int inx = 0, inxLoopSize = rAaRegTgtMeaSpcBdUncIrtListInq
				.getDataCount(); inx < inxLoopSize; inx++) {
			LData tAaRegTgtListInqDto = rAaRegTgtMeaSpcBdUncIrtListInq
					.getLData(inx);
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(tAaRegTgtListInqDto);

			// =============================================================================
			// ######### GeneralCodeBlock ##전체건수 + 1
			// =============================================================================
			tAaRegPrcNcnMgtDto.setLong("all_ncn",
					tAaRegPrcNcnMgtDto.getLong("all_ncn") + 1);
			if (KLDataConvertUtil.equals("Y",
					tAaRegTgtListInqDto.getString("mbs_sel_tgt_yn"))) {

				// =============================================================================
				// ######### GeneralCodeBlock ##생략건수 + 1,  MBS매각대상건수 + 1
				// =============================================================================
				tAaRegTgtListInqDto.setString("omss_yn", "Y");

				tAaRegPrcNcnMgtDto.setLong("omss_ncn",
						tAaRegPrcNcnMgtDto.getLong("omss_ncn") + 1);
				tAaRegPrcNcnMgtDto.setLong("mbs_sel_tgt_ncn",
						tAaRegPrcNcnMgtDto.getLong("mbs_sel_tgt_ncn") + 1);

				// =============================================================================
				// ######### GeneralCodeBlock ##continue
				// =============================================================================
				continue;
			}
			if (KLDataConvertUtil.equals("01",
					tAaRegTgtListInqDto.getString("ln_ss_cd")) // 대출상태(01:유지)
			) {
				if ((tAaRegTgtListInqDto.getBigDecimal("ln_bal"))
						.compareTo(new BigDecimal("0")) <= 0) {

					// =============================================================================
					// ######### GeneralCodeBlock ##생략건수 + 1
					// =============================================================================
					tAaRegTgtListInqDto.setString("omss_yn", "Y");
					tAaRegPrcNcnMgtDto.setLong("omss_ncn",
							tAaRegPrcNcnMgtDto.getLong("omss_ncn") + 1);

					// =============================================================================
					// ######### GeneralCodeBlock ##continue
					// =============================================================================
					continue;
				}
				if ((tAaRegTgtListInqDto.getString("pa_fsr_dt"))
						.compareTo("19000101") <= 0 // 납입응당일 <= '1900-01-01' 인 건은 Data 오류이므로 SKIP
				) {

					// =============================================================================
					// ######### GeneralCodeBlock ##생략건수 + 1
					// =============================================================================
					tAaRegTgtListInqDto.setString("omss_yn", "Y");
					tAaRegPrcNcnMgtDto.setLong("omss_ncn",
							tAaRegPrcNcnMgtDto.getLong("omss_ncn") + 1);

					// =============================================================================
					// ######### GeneralCodeBlock ##continue
					// =============================================================================
					continue;
				}

				//// 1. 연체작업의 연체대상 및 연체등록사유코드 CHECK -->>> (대출상태 : 유지)건 대상
				{

					// =============================================================================
					// ######### GeneralCodeBlock ##하위프로그램통과여부 초기화 셋팅 (유지건 체크시 조건으로만 사용)
					// =============================================================================
					boolean bLwptProgPagYn = false; //#b하위프로그램통과여부
					if (!bLwptProgPagYn
							&& (tAaRegTgtListInqDto.getString("pa_fsr_dt"))
									.compareTo("00010101") > 0) {

						// =============================================================================
						// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
						// =============================================================================
						// [i연체기산일산출]  

						iAaRckgDayCu.setString("in_dv", "2");
						iAaRckgDayCu.setString("in_dt",
								tAaRegTgtListInqDto.getString("pa_fsr_dt"));
						KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iAaRckgDayCu);
						rAaRckgDayCu = pcitRcvCommCpbc
								.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출
						if ((rAaRckgDayCu.getString("aa_st_dt"))
								.compareTo("19000101") > 0
								&& (rAaRckgDayCu.getString("aa_st_dt"))
										.compareTo(mtAaClcBsDt) <= 0) {

							// =============================================================================
							// ######### GeneralCodeBlock ##통과여부 셋팅 및 연체등록사유코드/연체발생일자 셋팅
							// =============================================================================
							bLwptProgPagYn = true;

							tAaRegTgtListInqDto
									.setString("aa_reg_rsn_cd", "11"); // 납입응당일경과
							tAaRegTgtListInqDto.setString("aa_occ_dt",
									rAaRckgDayCu.getString("aa_st_dt"));
							tAaRegTgtListInqDto.setString("aa_occ_bs_dt",
									tAaRegTgtListInqDto.getString("pa_fsr_dt"));

							LLog.debug
									.println("<대출상태:유지대상> - 납입응당일경과대상 : 연체발생일자("
											+ tAaRegTgtListInqDto
													.getString("aa_occ_dt")
											+ "),  연체발생기준일자("
											+ tAaRegTgtListInqDto
													.getString("aa_occ_bs_dt")
											+ ")");
						}
					}
					if (!bLwptProgPagYn
							&& (tAaRegTgtListInqDto.getString("nt_rpy_dt"))
									.compareTo("00010101") > 0) {

						// =============================================================================
						// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
						// =============================================================================
						// [i연체기산일산출]  

						iAaRckgDayCu.setString("in_dv", "2");
						iAaRckgDayCu.setString("in_dt",
								tAaRegTgtListInqDto.getString("nt_rpy_dt"));
						KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
						LProtocolInitializeUtil
								.primitiveLMultiInitialize(iAaRckgDayCu);
						rAaRckgDayCu = pcitRcvCommCpbc
								.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출
						if ((rAaRckgDayCu.getString("aa_st_dt"))
								.compareTo("00010101") > 0
								&& (rAaRckgDayCu.getString("aa_st_dt"))
										.compareTo(mtAaClcBsDt) <= 0) {

							// =============================================================================
							// ######### GeneralCodeBlock ##통과여부 셋팅 및 연체등록사유코드/연체발생일자 셋팅
							// =============================================================================
							bLwptProgPagYn = true;

							tAaRegTgtListInqDto
									.setString("aa_reg_rsn_cd", "12"); // 상환기일경과
							tAaRegTgtListInqDto.setString("aa_occ_dt",
									rAaRckgDayCu.getString("aa_st_dt"));
							tAaRegTgtListInqDto.setString("aa_occ_bs_dt",
									tAaRegTgtListInqDto.getString("nt_rpy_dt"));

							LLog.debug
									.println("<대출상태:유지대상> - 차기상환일경과대상 : 연체발생일자("
											+ tAaRegTgtListInqDto
													.getString("aa_occ_dt")
											+ "),  연체발생기준일자("
											+ tAaRegTgtListInqDto
													.getString("aa_occ_bs_dt")
											+ ")");
						}
					}
					if (!bLwptProgPagYn
							&& (tAaRegTgtListInqDto.getString("exp_dt"))
									.compareTo("00010101") > 0) {
						try {

							// =============================================================================
							// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
							// =============================================================================
							// [i연체기산일산출]  

							iAaRckgDayCu.setString("in_dv", "2");
							iAaRckgDayCu.setString("in_dt",
									tAaRegTgtListInqDto.getString("exp_dt"));
							KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
							LProtocolInitializeUtil
									.primitiveLMultiInitialize(iAaRckgDayCu);
							rAaRckgDayCu = pcitRcvCommCpbc
									.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출
							if ((rAaRckgDayCu.getString("aa_st_dt"))
									.compareTo(mtAaClcBsDt) <= 0) {
								if (KLDataConvertUtil.equals("104012",
										tAaRegTgtListInqDto
												.getString("lon_pdt_cd"))
										&& KLDataConvertUtil
												.equals("06",
														tAaRegTgtListInqDto
																.getString("rpy_mth_cd")) //플러스카드대출(104012)중 상환방법이 '06.리볼빙'인 건은 만기일자가 '0001-01-01'이므로 연체대상에서 제외한다
								) {

									// =============================================================================
									// ######### GeneralCodeBlock ##생략건수 + 1
									// =============================================================================
									tAaRegTgtListInqDto.setString("omss_yn",
											"Y");
									tAaRegPrcNcnMgtDto.setLong("omss_ncn",
											tAaRegPrcNcnMgtDto
													.getLong("omss_ncn") + 1);

									// =============================================================================
									// ######### GeneralCodeBlock ##continue
									// =============================================================================
									continue;
								} else if (KLDataConvertUtil.notEquals("03",
										tAaRegTgtListInqDto
												.getString("rpy_mth_cd"))
										&& KLDataConvertUtil
												.notEquals(
														"05",
														tAaRegTgtListInqDto
																.getString("rpy_mth_cd")) //상환방법이'03.원리금균등'인 경우 연체로 등록에서 제외하고, 다음 로직으로. (20160819  거치후할부 추가)
								) {

									// =============================================================================
									// ######### GeneralCodeBlock ##통과여부 셋팅 및 연체등록사유코드/연체발생일자 셋팅
									// =============================================================================
									bLwptProgPagYn = true;

									tAaRegTgtListInqDto.setString(
											"aa_reg_rsn_cd", "12"); // 상환기일경과
									tAaRegTgtListInqDto.setString("aa_occ_dt",
											rAaRckgDayCu.getString("aa_st_dt"));
									tAaRegTgtListInqDto.setString(
											"aa_occ_bs_dt", tAaRegTgtListInqDto
													.getString("exp_dt"));

									LLog.debug
											.println("<대출상태:유지대상> - 만기일경과대상 : 연체발생일자("
													+ tAaRegTgtListInqDto
															.getString("aa_occ_dt")
													+ "),  연체발생기준일자("
													+ tAaRegTgtListInqDto
															.getString("aa_occ_bs_dt")
													+ ")");
								}
							}
						} catch (Exception e) {

							// =============================================================================
							// ######### GeneralCodeBlock ##만기일자 계산오류 통과 (영업일 테이블에 데이터가 없을때 발생)
							// =============================================================================
							LLog.debug
									.println("<<만기일자 기준 연체기산일 산출오류> ==>>>  영업일 데이터 없을때 발생함.");
						}
					}
					if (!bLwptProgPagYn) {
						if (KLDataConvertUtil.equals("104001",
								tAaRegTgtListInqDto.getString("lon_pdt_cd"))
								|| KLDataConvertUtil.equals("106003",
										tAaRegTgtListInqDto
												.getString("lon_pdt_cd"))
								|| KLDataConvertUtil.equals("106004",
										tAaRegTgtListInqDto
												.getString("lon_pdt_cd"))
								|| KLDataConvertUtil.equals("107001",
										tAaRegTgtListInqDto
												.getString("lon_pdt_cd"))
								|| KLDataConvertUtil.equals("107002",
										tAaRegTgtListInqDto
												.getString("lon_pdt_cd"))
								|| KLDataConvertUtil.equals("104020",
										tAaRegTgtListInqDto
												.getString("lon_pdt_cd"))
								|| KLDataConvertUtil.equals("104021",
										tAaRegTgtListInqDto
												.getString("lon_pdt_cd"))) {
							try {

								// =============================================================================
								// ######### GeneralCodeBlock ##퇴직조직원대출명세조회  입력값 세팅
								// =============================================================================
								// [i퇴직조직원대출명세조회]  

								iRttOrzOlnDetsInq.setString("ln_no",
										tAaRegTgtListInqDto.getString("ln_no"));
								iRttOrzOlnDetsInq.setLong("ln_seq",
										tAaRegTgtListInqDto.getLong("ln_seq"));
								commonDao = new LCommonDao(
										"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvRttOrzOlnDets",
										iRttOrzOlnDetsInq); //##퇴직조직원대출명세조회
								rRttOrzOlnDetsInq = commonDao
										.executeQueryOnlySingle();
								if (KLDataConvertUtil.equals("107001666598",
										tAaRegTgtListInqDto.getString("ln_no")) //임시로... 2012.07.24  임직원대출 예외금리 적용 - 전미옥씨 요청(김민주 소마 5473(2012-07-18 18:28))
								) {

									// =============================================================================
									// ######### GeneralCodeBlock ##임시값 확인 및 셋팅
									// =============================================================================
									if (mtBsDt.compareTo("20121007") > 0) {
										rRttOrzOlnDetsInq.setString("rtt_dt",
												"20121007");
										rRttOrzOlnDetsInq.setString(
												"rtpy_pn_dt", "20121007");
									} else {
										rRttOrzOlnDetsInq.setString("rtt_dt",
												"00010101");
										rRttOrzOlnDetsInq.setString(
												"rtpy_pn_dt", "00010101");
									}
								}
								if (!bLwptProgPagYn
										&& (rRttOrzOlnDetsInq
												.getString("rtt_dt"))
												.compareTo("00010101") > 0
										&& KLDataConvertUtil
												.equals(tAaRegTgtListInqDto
														.getString("lon_pdt_cd"),
														"106004") // 설계사 대출(106004:보증보험담보)
								) {

									// =============================================================================
									// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
									// =============================================================================
									// [i연체기산일산출]

									iAaRckgDayCu.setString("in_dv", "2");
									iAaRckgDayCu.setString("in_dt",
											rRttOrzOlnDetsInq
													.getString("rtt_dt"));
									KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
									LProtocolInitializeUtil
											.primitiveLMultiInitialize(iAaRckgDayCu);
									rAaRckgDayCu = pcitRcvCommCpbc
											.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출
									if ((rAaRckgDayCu.getString("aa_st_dt"))
											.compareTo("19000101") > 0
											&& (rAaRckgDayCu
													.getString("aa_st_dt"))
													.compareTo(mtAaClcBsDt) <= 0) {

										// =============================================================================
										// ######### GeneralCodeBlock ##통과여부 셋팅 및 연체등록사유코드/연체발생일자 셋팅
										// =============================================================================
										bLwptProgPagYn = true;

										tAaRegTgtListInqDto.setString(
												"aa_reg_rsn_cd", "21"); // 퇴직및해촉
										tAaRegTgtListInqDto.setString(
												"aa_occ_dt", rAaRckgDayCu
														.getString("aa_st_dt"));
										tAaRegTgtListInqDto.setString(
												"aa_occ_bs_dt",
												rRttOrzOlnDetsInq
														.getString("rtt_dt"));

										LLog.debug
												.println("<대출상태:유지대상> - 퇴직및해촉대상(설계사) : 연체발생일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_dt")
														+ "),  연체발생기준일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_bs_dt")
														+ ")");
									} else {

										// =============================================================================
										// ######### GeneralCodeBlock ##생략건수 + 1
										// =============================================================================
										tAaRegTgtListInqDto.setString(
												"omss_yn", "Y");
										tAaRegPrcNcnMgtDto
												.setLong(
														"omss_ncn",
														tAaRegPrcNcnMgtDto
																.getLong("omss_ncn") + 1);

										// =============================================================================
										// ######### GeneralCodeBlock ##continue
										// =============================================================================
										continue;
									}
								}
								if (!bLwptProgPagYn
										&& (rRttOrzOlnDetsInq
												.getString("rtpy_pn_dt"))
												.compareTo("00010101") > 0 // 내근사원대출
								) {

									// =============================================================================
									// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
									// =============================================================================
									// [i연체기산일산출]  

									iAaRckgDayCu.setString("in_dv", "2");
									iAaRckgDayCu.setString("in_dt",
											rRttOrzOlnDetsInq
													.getString("rtpy_pn_dt"));
									KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
									LProtocolInitializeUtil
											.primitiveLMultiInitialize(iAaRckgDayCu);
									rAaRckgDayCu = pcitRcvCommCpbc
											.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출
									if ((rAaRckgDayCu.getString("aa_st_dt"))
											.compareTo("19000101") > 0
											&& (rAaRckgDayCu
													.getString("aa_st_dt"))
													.compareTo(mtAaClcBsDt) <= 0) {

										// =============================================================================
										// ######### GeneralCodeBlock ##통과여부 셋팅 및 연체등록사유코드/연체발생일자 셋팅
										// =============================================================================
										bLwptProgPagYn = true;

										tAaRegTgtListInqDto.setString(
												"aa_reg_rsn_cd", "21"); // 퇴직및해촉
										tAaRegTgtListInqDto.setString(
												"aa_occ_dt", rAaRckgDayCu
														.getString("aa_st_dt"));
										tAaRegTgtListInqDto
												.setString(
														"aa_occ_bs_dt",
														rRttOrzOlnDetsInq
																.getString("rtpy_pn_dt"));

										LLog.debug
												.println("<대출상태:유지대상> - 퇴직및해촉대상(내근사원) : 연체발생일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_dt")
														+ "),  연체발생기준일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_bs_dt")
														+ ")");
									} else {

										// =============================================================================
										// ######### GeneralCodeBlock ##생략건수 + 1
										// =============================================================================
										tAaRegTgtListInqDto.setString(
												"omss_yn", "Y");
										tAaRegPrcNcnMgtDto
												.setLong(
														"omss_ncn",
														tAaRegPrcNcnMgtDto
																.getLong("omss_ncn") + 1);

										// =============================================================================
										// ######### GeneralCodeBlock ##continue
										// =============================================================================
										continue;
									}
								}
							} catch (LNotFoundException e) {
								if (!bLwptProgPagYn
										&& KLDataConvertUtil
												.equals(tAaRegTgtListInqDto
														.getString("lon_pdt_cd"),
														"104020") // IRA임직원신용대출
								) {
									try {

										// =============================================================================
										// ######### GeneralCodeBlock ##임직원신용대출가입입금수령일자조회  입력값 세팅
										// =============================================================================
										// [i임직원신용대출가입입금수령일자조회]  

										iEismCrdLnSbcRemRecpDtInq.setString(
												"ig_cs_no", tAaRegTgtListInqDto
														.getString("ig_cs_no"));
										commonDao = new LCommonDao(
												"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvEismCrdLnSbcRemRecpDt",
												iEismCrdLnSbcRemRecpDtInq); //##임직원신용대출가입입금수령일자조회
										rEismCrdLnSbcRemRecpDtInq = commonDao
												.executeQueryOnlySingle();
										if ((rEismCrdLnSbcRemRecpDtInq
												.getString("sbc_amt_recp_dt"))
												.compareTo("00010101") > 0) {

											// =============================================================================
											// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
											// =============================================================================
											// [i연체기산일산출]  

											iAaRckgDayCu
													.setString("in_dv", "2");
											iAaRckgDayCu
													.setString(
															"in_dt",
															rEismCrdLnSbcRemRecpDtInq
																	.getString("sbc_amt_recp_dt"));
											KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
											LProtocolInitializeUtil
													.primitiveLMultiInitialize(iAaRckgDayCu);
											rAaRckgDayCu = pcitRcvCommCpbc
													.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출
											if ((rAaRckgDayCu
													.getString("aa_st_dt"))
													.compareTo("19000101") > 0
													&& (rAaRckgDayCu
															.getString("aa_st_dt"))
															.compareTo(mtAaClcBsDt) <= 0) {

												// =============================================================================
												// ######### GeneralCodeBlock ##통과여부 셋팅 및 연체등록사유코드/연체발생일자 셋팅
												// =============================================================================
												bLwptProgPagYn = true;

												tAaRegTgtListInqDto.setString(
														"aa_reg_rsn_cd", "12"); // 상환기일경과
												tAaRegTgtListInqDto
														.setString(
																"aa_occ_dt",
																rAaRckgDayCu
																		.getString("aa_st_dt"));
												tAaRegTgtListInqDto
														.setString(
																"aa_occ_bs_dt",
																rEismCrdLnSbcRemRecpDtInq
																		.getString("sbc_amt_recp_dt"));

												LLog.debug
														.println("<대출상태:유지대상> - (IPA임직원신용대출) : 연체발생일자("
																+ tAaRegTgtListInqDto
																		.getString("aa_occ_dt")
																+ "),  연체발생기준일자("
																+ tAaRegTgtListInqDto
																		.getString("aa_occ_bs_dt")
																+ ")");
											} else {

												// =============================================================================
												// ######### GeneralCodeBlock ##생략건수 + 1
												// =============================================================================
												tAaRegTgtListInqDto.setString(
														"omss_yn", "Y");
												tAaRegPrcNcnMgtDto
														.setLong(
																"omss_ncn",
																tAaRegPrcNcnMgtDto
																		.getLong("omss_ncn") + 1);

												// =============================================================================
												// ######### GeneralCodeBlock ##continue
												// =============================================================================
												continue;
											}
										} else {

											// =============================================================================
											// ######### GeneralCodeBlock ##생략건수 + 1
											// =============================================================================
											tAaRegTgtListInqDto.setString(
													"omss_yn", "Y");
											tAaRegPrcNcnMgtDto
													.setLong(
															"omss_ncn",
															tAaRegPrcNcnMgtDto
																	.getLong("omss_ncn") + 1);

											// =============================================================================
											// ######### GeneralCodeBlock ##continue
											// =============================================================================
											continue;
										}
									} catch (LNotFoundException e1) {

										// =============================================================================
										// ######### GeneralCodeBlock ##생략건수 + 1
										// =============================================================================
										tAaRegTgtListInqDto.setString(
												"omss_yn", "Y");
										tAaRegPrcNcnMgtDto
												.setLong(
														"omss_ncn",
														tAaRegPrcNcnMgtDto
																.getLong("omss_ncn") + 1);

										// =============================================================================
										// ######### GeneralCodeBlock ##continue
										// =============================================================================
										continue;
									}
								}
								if (!bLwptProgPagYn
										&& KLDataConvertUtil
												.equals(tAaRegTgtListInqDto
														.getString("lon_pdt_cd"),
														"104021") // 우리사주신용대출
								) {
									try {

										// =============================================================================
										// ######### GeneralCodeBlock ##우리사주신용대출해지일자조회  입력값 세팅
										// =============================================================================
										// [i우리사주신용대출해지일자조회]  

										iEsopCrdLnTrmDtInq.setString(
												"ig_cs_no", tAaRegTgtListInqDto
														.getString("ig_cs_no"));
										iEsopCrdLnTrmDtInq
												.setString(
														"lon_pdt_cd",
														tAaRegTgtListInqDto
																.getString("lon_pdt_cd"));
										iEsopCrdLnTrmDtInq
												.setString(
														"lon_pdt_sce_no",
														tAaRegTgtListInqDto
																.getString("lon_pdt_sce_no"));
										commonDao = new LCommonDao(
												"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvEsopCrdLnTrmDt",
												iEsopCrdLnTrmDtInq); //##우리사주신용대출해지일자조회
										rEsopCrdLnTrmDtInq = commonDao
												.executeQueryOnlySingle();
										if ((rEsopCrdLnTrmDtInq
												.getString("esop_trm_dt"))
												.compareTo("00010101") > 0) {

											// =============================================================================
											// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
											// =============================================================================
											// [i연체기산일산출]  

											iAaRckgDayCu
													.setString("in_dv", "2");
											iAaRckgDayCu
													.setString(
															"in_dt",
															rEsopCrdLnTrmDtInq
																	.getString("esop_trm_dt"));
											KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
											LProtocolInitializeUtil
													.primitiveLMultiInitialize(iAaRckgDayCu);
											rAaRckgDayCu = pcitRcvCommCpbc
													.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출
											if ((rAaRckgDayCu
													.getString("aa_st_dt"))
													.compareTo("19000101") > 0
													&& (rAaRckgDayCu
															.getString("aa_st_dt"))
															.compareTo(mtAaClcBsDt) <= 0) {

												// =============================================================================
												// ######### GeneralCodeBlock ##통과여부 셋팅 및 연체등록사유코드/연체발생일자 셋팅
												// =============================================================================
												bLwptProgPagYn = true;

												tAaRegTgtListInqDto.setString(
														"aa_reg_rsn_cd", "12"); // 상환기일경과
												tAaRegTgtListInqDto
														.setString(
																"aa_occ_dt",
																rAaRckgDayCu
																		.getString("aa_st_dt"));
												tAaRegTgtListInqDto
														.setString(
																"aa_occ_bs_dt",
																rEsopCrdLnTrmDtInq
																		.getString("esop_trm_dt"));

												LLog.debug
														.println("<대출상태:유지대상> - (우리사주신용대출) : 연체발생일자("
																+ tAaRegTgtListInqDto
																		.getString("aa_occ_dt")
																+ "),  연체발생기준일자("
																+ tAaRegTgtListInqDto
																		.getString("aa_occ_bs_dt")
																+ ")");
											} else {

												// =============================================================================
												// ######### GeneralCodeBlock ##생략건수 + 1
												// =============================================================================
												tAaRegTgtListInqDto.setString(
														"omss_yn", "Y");
												tAaRegPrcNcnMgtDto
														.setLong(
																"omss_ncn",
																tAaRegPrcNcnMgtDto
																		.getLong("omss_ncn") + 1);

												// =============================================================================
												// ######### GeneralCodeBlock ##continue
												// =============================================================================
												continue;
											}
										} else {

											// =============================================================================
											// ######### GeneralCodeBlock ##생략건수 + 1
											// =============================================================================
											tAaRegTgtListInqDto.setString(
													"omss_yn", "Y");
											tAaRegPrcNcnMgtDto
													.setLong(
															"omss_ncn",
															tAaRegPrcNcnMgtDto
																	.getLong("omss_ncn") + 1);

											// =============================================================================
											// ######### GeneralCodeBlock ##continue
											// =============================================================================
											continue;
										}
									} catch (LNotFoundException e2) {

										// =============================================================================
										// ######### GeneralCodeBlock ##생략건수 + 1
										// =============================================================================
										tAaRegTgtListInqDto.setString(
												"omss_yn", "Y");
										tAaRegPrcNcnMgtDto
												.setLong(
														"omss_ncn",
														tAaRegPrcNcnMgtDto
																.getLong("omss_ncn") + 1);

										// =============================================================================
										// ######### GeneralCodeBlock ##continue
										// =============================================================================
										continue;
									}
								}
							}
						} else {

							// =============================================================================
							// ######### GeneralCodeBlock ##생략건수 + 1
							// =============================================================================
							tAaRegTgtListInqDto.setString("omss_yn", "Y");
							tAaRegPrcNcnMgtDto.setLong("omss_ncn",
									tAaRegPrcNcnMgtDto.getLong("omss_ncn") + 1);

							// =============================================================================
							// ######### GeneralCodeBlock ##continue
							// =============================================================================
							continue;
						}
					}
					if (!bLwptProgPagYn // 아무데도 걸리지 않았을때,
					) {

						// =============================================================================
						// ######### GeneralCodeBlock ##생략건수 + 1
						// =============================================================================
						tAaRegTgtListInqDto.setString("omss_yn", "Y");
						tAaRegPrcNcnMgtDto.setLong("omss_ncn",
								tAaRegPrcNcnMgtDto.getLong("omss_ncn") + 1);

						// =============================================================================
						// ######### GeneralCodeBlock ##continue
						// =============================================================================
						continue;
					}
					if (KLDataConvertUtil.notEquals("Y",
							tAaRegTgtListInqDto.getString("omss_yn"))) {
						try {

							// =============================================================================
							// ######### GeneralCodeBlock ##자동이체청구내역조회  입력값 세팅
							// =============================================================================
							// [i자동이체청구내역조회]  

							iAutTrsRqHisInq.setString("ln_no",
									tAaRegTgtListInqDto.getString("ln_no"));
							iAutTrsRqHisInq.setLong("ln_seq",
									tAaRegTgtListInqDto.getLong("ln_seq"));
							iAutTrsRqHisInq.setString("rq_ss_cd", "0");
							commonDao = new LCommonDao(
									"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvAutTrsRqHis",
									iAutTrsRqHisInq); //##자동이체청구내역조회
							rAutTrsRqHisInq = commonDao
									.executeQueryOnlySingle();
							if (KLDataConvertUtil.equals(
									rAutTrsRqHisInq.getString("rq_dt"),
									rAutTrsRqHisInq.getString("orrq_dt"))
									|| (KLDataConvertUtil.equals("20",
											rAutTrsRqHisInq
													.getString("rq_mth_cd"))
											&& KLDataConvertUtil
													.equals(rAutTrsRqHisInq
															.getString("orrq_dt"),
															"20100105") && (KLDataConvertUtil
											.equals(rAutTrsRqHisInq
													.getString("rq_dt"),
													"20100105") || KLDataConvertUtil
											.equals(rAutTrsRqHisInq
													.getString("rq_dt"),
													"20100110")))) {

								// =============================================================================
								// ######### GeneralCodeBlock ##사후연체기본연체상태조회  입력값 세팅
								// =============================================================================
								// [i사후연체기본연체상태조회]  

								iAffcAaBasAaSsInq.setString("ln_no",
										tAaRegTgtListInqDto.getString("ln_no"));
								iAffcAaBasAaSsInq.setLong("ln_seq",
										tAaRegTgtListInqDto.getLong("ln_seq"));
								commonDao = new LCommonDao(
										"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvAffcAaBasAaSs",
										iAffcAaBasAaSsInq); //##사후연체기본연체상태조회
								rAffcAaBasAaSsInq = commonDao
										.executeQueryOnlySingle();
								if (KLDataConvertUtil
										.equals("1", rAffcAaBasAaSsInq
												.getString("aa_dv_cd")) // 연체구분코드(1:연체상태), 현재 연체 등록중인 상태
								) {

									// =============================================================================
									// ######### GeneralCodeBlock ##연체발생일자변경 / 연체자동이체진행여부 셋팅
									// =============================================================================
									tAaRegTgtListInqDto.setString("aa_occ_dt",
											rAffcAaBasAaSsInq
													.getString("aa_occ_dt"));
									tAaRegTgtListInqDto.setString(
											"aa_occ_bs_dt", rAffcAaBasAaSsInq
													.getString("aa_occ_bs_dt"));
									tAaRegTgtListInqDto.setString(
											"aa_aut_trs_prs_yn", "Y");
								} else {// 연체구분코드(0:연체해제상태, 2:신규연체상태)
								// 연체구분코드(0:연체해제상태, 2:신규연체상태)

									// =============================================================================
									// ######### GeneralCodeBlock ##생략건수 + 1,  자동이체청구건수+ 1
									// =============================================================================
									tAaRegTgtListInqDto.setString("omss_yn",
											"Y");

									tAaRegPrcNcnMgtDto.setLong("omss_ncn",
											tAaRegPrcNcnMgtDto
													.getLong("omss_ncn") + 1);
									tAaRegPrcNcnMgtDto
											.setLong(
													"aut_trs_rq_ncn",
													tAaRegPrcNcnMgtDto
															.getLong("aut_trs_rq_ncn") + 1);

									// =============================================================================
									// ######### GeneralCodeBlock ##continue
									// =============================================================================
									continue;
								}
							}
						} catch (LNotFoundException e1) {

							// =============================================================================
							// ######### GeneralCodeBlock ##자동이체청구대상 아님,  연체제외 안함!
							// =============================================================================
							LLog.debug.println("자동이체 청구대상 아님,  통과!");
						}
					}
				}
			} else if (KLDataConvertUtil.equals("08",
					tAaRegTgtListInqDto.getString("ln_ss_cd")) // 대출상태(08:완제)
			) {
				if (KLDataConvertUtil.equals("Y",
						tAaRegTgtListInqDto.getString("spc_bd_reg_yn"))) {

					//// 2. 연체작업의 연체대상 및 연체등록사유코드 CHECK -->>> (대출상태 : 완제, 특수채권등록상태)건 대상
					{
						try {

							// =============================================================================
							// ######### GeneralCodeBlock ##특수채권기본조회  입력값 세팅
							// =============================================================================
							// [i특수채권기본조회]  

							iSpcBdBasInq.setString("ln_no",
									tAaRegTgtListInqDto.getString("ln_no"));
							iSpcBdBasInq.setString("rels_dt", "00010101");
							commonDao = new LCommonDao(
									"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvSpcBdBas",
									iSpcBdBasInq); //##특수채권기본조회
							rSpcBdBasInq = commonDao.executeQueryOnlySingle();
							if ((rSpcBdBasInq.getBigDecimal("spc_bd_bal"))
									.compareTo(new BigDecimal("0")) > 0) {
								if (KLDataConvertUtil.notEquals("02",
										tAaRegTgtListInqDto
												.getString("ins_bd_ss_cd"))
										&& KLDataConvertUtil
												.notEquals(
														"03",
														tAaRegTgtListInqDto
																.getString("ins_bd_ss_cd"))
										&& KLDataConvertUtil
												.notEquals(
														"04",
														tAaRegTgtListInqDto
																.getString("ins_bd_ss_cd"))
										&& KLDataConvertUtil
												.notEquals(
														"05",
														tAaRegTgtListInqDto
																.getString("ins_bd_ss_cd"))
										&& KLDataConvertUtil
												.notEquals(
														"09",
														tAaRegTgtListInqDto
																.getString("ins_bd_ss_cd"))
										&& KLDataConvertUtil
												.notEquals(
														"12",
														tAaRegTgtListInqDto
																.getString("ins_bd_ss_cd"))) {

									// =============================================================================
									// ######### GeneralCodeBlock ##연체등록사유코드/연체발생일자 셋팅
									// =============================================================================
									tAaRegTgtListInqDto.setString(
											"aa_reg_rsn_cd", "12"); // 상환기일경과
									tAaRegTgtListInqDto.setString("aa_occ_dt",
											rSpcBdBasInq.getString("incp_dt"));
									tAaRegTgtListInqDto.setString(
											"aa_occ_bs_dt",
											rSpcBdBasInq.getString("incp_dt"));

									LLog.debug
											.println("<대출상태:완제> - 특수채권(특수채권편입일자) : 연체발생일자("
													+ tAaRegTgtListInqDto
															.getString("aa_occ_dt")
													+ "),  연체발생기준일자("
													+ tAaRegTgtListInqDto
															.getString("aa_occ_bs_dt")
													+ ")");
								} else {
									if ((tAaRegTgtListInqDto
											.getString("pa_fsr_dt"))
											.compareTo("19000101") >= 0) {

										// =============================================================================
										// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
										// =============================================================================
										// [i연체기산일산출]  

										iAaRckgDayCu.setString("in_dv", "2");
										iAaRckgDayCu
												.setString(
														"in_dt",
														tAaRegTgtListInqDto
																.getString("pa_fsr_dt"));
										KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
										LProtocolInitializeUtil
												.primitiveLMultiInitialize(iAaRckgDayCu);
										rAaRckgDayCu = pcitRcvCommCpbc
												.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출

										// =============================================================================
										// ######### GeneralCodeBlock ##연체등록사유코드/연체발생일자 셋팅
										// =============================================================================
										tAaRegTgtListInqDto.setString(
												"aa_reg_rsn_cd", "12"); // 상환기일경과
										tAaRegTgtListInqDto.setString(
												"aa_occ_dt", rAaRckgDayCu
														.getString("aa_st_dt"));
										tAaRegTgtListInqDto
												.setString(
														"aa_occ_bs_dt",
														tAaRegTgtListInqDto
																.getString("pa_fsr_dt"));

										LLog.debug
												.println("<대출상태:완제> - 특수채권(납입응당일) : 연체발생일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_dt")
														+ "),  연체발생기준일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_bs_dt")
														+ ")");
									} else {

										// =============================================================================
										// ######### GeneralCodeBlock ##연체등록사유코드/연체발생일자 셋팅
										// =============================================================================
										tAaRegTgtListInqDto.setString(
												"aa_reg_rsn_cd", "12"); // 상환기일경과
										tAaRegTgtListInqDto.setString(
												"aa_occ_dt", rSpcBdBasInq
														.getString("incp_dt"));
										tAaRegTgtListInqDto.setString(
												"aa_occ_bs_dt", rSpcBdBasInq
														.getString("incp_dt"));

										LLog.debug
												.println("<대출상태:완제> - 특수채권(특수채권편입일자) : 연체발생일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_dt")
														+ "),  연체발생기준일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_bs_dt")
														+ ")");
									}

									// =============================================================================
									// ######### GeneralCodeBlock ##특수채권건수 + 1
									// =============================================================================
									tAaRegPrcNcnMgtDto
											.setBigDecimal(
													"spc_bd_ncn",
													(tAaRegPrcNcnMgtDto
															.getBigDecimal("spc_bd_ncn"))
															.add(new BigDecimal(
																	"1")));
								}
							} else {

								// =============================================================================
								// ######### GeneralCodeBlock ##생략건수 + 1
								// =============================================================================
								tAaRegTgtListInqDto.setString("omss_yn", "Y");
								tAaRegPrcNcnMgtDto
										.setLong("omss_ncn", tAaRegPrcNcnMgtDto
												.getLong("omss_ncn") + 1);

								// =============================================================================
								// ######### GeneralCodeBlock ##continue
								// =============================================================================
								continue;
							}
						} catch (LNotFoundException e) {

							// =============================================================================
							// ######### GeneralCodeBlock ##생략건수 + 1
							// =============================================================================
							tAaRegTgtListInqDto.setString("omss_yn", "Y");
							tAaRegPrcNcnMgtDto.setLong("omss_ncn",
									tAaRegPrcNcnMgtDto.getLong("omss_ncn") + 1);

							// =============================================================================
							// ######### GeneralCodeBlock ##continue
							// =============================================================================
							continue;
						}
					}
				} else {

					//// 3. 연체작업의 연체대상 및 연체등록사유코드 CHECK -->>> (대출상태 : 완제, 미결대상)건 대상
					{

						// =============================================================================
						// ######### GeneralCodeBlock ##여신미결발생내역조회  입력값 세팅
						// =============================================================================
						// [i여신미결발생내역조회]  

						iLonInatOccHisInq.setString("ln_no",
								tAaRegTgtListInqDto.getString("ln_no"));
						iLonInatOccHisInq.setString("inat_dv_cd", "20"); //미수
						iLonInatOccHisInq.setString("inat_dtl_cd", "52"); //미수채권
						commonDao = new LCommonDao(
								"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvLonInatOccHis",
								iLonInatOccHisInq); //##여신미결발생내역조회
						rLonInatOccHisInq = commonDao.executeQueryOnlySingle();
						if ((rLonInatOccHisInq.getBigDecimal("inat_bal"))
								.compareTo(new BigDecimal("0")) > 0) {
							if ((tAaRegTgtListInqDto.getString("cmpe_ls_dt"))
									.compareTo("00010101") > 0) {

								// =============================================================================
								// ######### GeneralCodeBlock ##연체등록사유코드/연체발생일자 셋팅
								// =============================================================================
								tAaRegTgtListInqDto.setString("aa_reg_rsn_cd",
										"11"); // 납입응당일경과
								tAaRegTgtListInqDto.setString("aa_occ_dt",
										KDateUtil.addDay(tAaRegTgtListInqDto
												.getString("cmpe_ls_dt"), 1));
								tAaRegTgtListInqDto.setString("aa_occ_bs_dt",
										KDateUtil.addDay(tAaRegTgtListInqDto
												.getString("cmpe_ls_dt"), 1));

								LLog.debug
										.println("<대출상태:완제> - 미결대상(이수최종일자) : 연체발생일자("
												+ tAaRegTgtListInqDto
														.getString("aa_occ_dt")
												+ "),  연체발생기준일자("
												+ tAaRegTgtListInqDto
														.getString("aa_occ_bs_dt")
												+ ")");
							} else {
								if ((tAaRegTgtListInqDto.getString("pa_fsr_dt"))
										.compareTo("00010101") > 0) {

									// =============================================================================
									// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
									// =============================================================================
									// [i연체기산일산출]  

									iAaRckgDayCu.setString("in_dv", "2");
									iAaRckgDayCu.setString("in_dt",
											tAaRegTgtListInqDto
													.getString("pa_fsr_dt"));
									KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
									LProtocolInitializeUtil
											.primitiveLMultiInitialize(iAaRckgDayCu);
									rAaRckgDayCu = pcitRcvCommCpbc
											.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출

									// =============================================================================
									// ######### GeneralCodeBlock ##연체등록사유코드/연체발생일자 셋팅
									// =============================================================================
									tAaRegTgtListInqDto.setString(
											"aa_reg_rsn_cd", "11"); // 납입응당일경과
									tAaRegTgtListInqDto.setString("aa_occ_dt",
											rAaRckgDayCu.getString("aa_st_dt"));
									tAaRegTgtListInqDto.setString(
											"aa_occ_bs_dt", tAaRegTgtListInqDto
													.getString("pa_fsr_dt"));

									LLog.debug
											.println("<대출상태:완제> - 미결대상(납입응당일자) : 연체발생일자("
													+ tAaRegTgtListInqDto
															.getString("aa_occ_dt")
													+ "),  연체발생기준일자("
													+ tAaRegTgtListInqDto
															.getString("aa_occ_bs_dt")
													+ ")");
								} else {
									if ((tAaRegTgtListInqDto
											.getString("nt_rpy_dt"))
											.compareTo("00010101") > 0) {

										// =============================================================================
										// ######### GeneralCodeBlock ##연체기산일산출  입력값 세팅
										// =============================================================================
										// [i연체기산일산출]  

										iAaRckgDayCu.setString("in_dv", "2");
										iAaRckgDayCu
												.setString(
														"in_dt",
														tAaRegTgtListInqDto
																.getString("nt_rpy_dt"));
										KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
										LProtocolInitializeUtil
												.primitiveLMultiInitialize(iAaRckgDayCu);
										rAaRckgDayCu = pcitRcvCommCpbc
												.cmptAaRckgDay(iAaRckgDayCu); //##연체기산일산출

										// =============================================================================
										// ######### GeneralCodeBlock ##연체등록사유코드/연체발생일자 셋팅
										// =============================================================================
										tAaRegTgtListInqDto.setString(
												"aa_reg_rsn_cd", "11"); // 납입응당일경과
										tAaRegTgtListInqDto.setString(
												"aa_occ_dt", rAaRckgDayCu
														.getString("aa_st_dt"));
										tAaRegTgtListInqDto
												.setString(
														"aa_occ_bs_dt",
														tAaRegTgtListInqDto
																.getString("nt_rpy_dt"));

										LLog.debug
												.println("<대출상태:완제> - 미결대상(차기상환일자) : 연체발생일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_dt")
														+ "),  연체발생기준일자("
														+ tAaRegTgtListInqDto
																.getString("aa_occ_bs_dt")
														+ ")");
									} else {

										// =============================================================================
										// ######### GeneralCodeBlock ##write  입력값 세팅 (*** PND 52 ERR ***)
										// =============================================================================
										// [i사후연체기본연체등록]

										iAffcAaBasAaReg = "<< PND 52 ERR - 미결대상 >>  "
												+ "대출번호 : "
												+ tAaRegTgtListInqDto
														.getString("ln_no")
												+ ", "
												+ "대출일련번호 : "
												+ tAaRegTgtListInqDto
														.getLong("ln_seq")
												+ ", "
												+ "미결잔액 : "
												+ rLonInatOccHisInq
														.getBigDecimal("inat_bal")
												+ ", "
												+ "이수최종일자 : "
												+ tAaRegTgtListInqDto
														.getString("cmpe_ls_dt")
												+ ", "
												+ "납입응당일자 : "
												+ tAaRegTgtListInqDto
														.getString("pa_fsr_dt")
												+ ", "
												+ "차기상환일자 : "
												+ tAaRegTgtListInqDto
														.getString("nt_rpy_dt")
												+ "\n\n";
										writer.writeBytes(iAffcAaBasAaReg);

										// =============================================================================
										// ######### GeneralCodeBlock ##오류건수 + 1
										// =============================================================================
										tAaRegTgtListInqDto.setString(
												"omss_yn", "Y");
										tAaRegPrcNcnMgtDto
												.setLong(
														"err_ncn",
														tAaRegPrcNcnMgtDto
																.getLong("err_ncn") + 1);

										// =============================================================================
										// ######### GeneralCodeBlock ##continue
										// =============================================================================
										continue;
									}
								}
							}
							if (KLDataConvertUtil.equals("02",
									tAaRegTgtListInqDto
											.getString("ins_bd_ss_cd"))
									|| KLDataConvertUtil.equals("03",
											tAaRegTgtListInqDto
													.getString("ins_bd_ss_cd"))
									|| KLDataConvertUtil.equals("04",
											tAaRegTgtListInqDto
													.getString("ins_bd_ss_cd"))
									|| KLDataConvertUtil.equals("05",
											tAaRegTgtListInqDto
													.getString("ins_bd_ss_cd"))
									|| KLDataConvertUtil.equals("09",
											tAaRegTgtListInqDto
													.getString("ins_bd_ss_cd"))
									|| KLDataConvertUtil.equals("10",
											tAaRegTgtListInqDto
													.getString("ins_bd_ss_cd"))
									|| KLDataConvertUtil.equals("11",
											tAaRegTgtListInqDto
													.getString("ins_bd_ss_cd"))
									|| KLDataConvertUtil.equals("12",
											tAaRegTgtListInqDto
													.getString("ins_bd_ss_cd"))) {

								// =============================================================================
								// ######### GeneralCodeBlock ##생략건수 + 1
								// =============================================================================
								tAaRegTgtListInqDto.setString("omss_yn", "Y");
								tAaRegPrcNcnMgtDto
										.setLong("omss_ncn", tAaRegPrcNcnMgtDto
												.getLong("omss_ncn") + 1);

								// =============================================================================
								// ######### GeneralCodeBlock ##continue
								// =============================================================================
								continue;
							}

							// =============================================================================
							// ######### GeneralCodeBlock ##미수이자대상건수 + 1
							// =============================================================================
							tAaRegPrcNcnMgtDto.setLong("unc_irt_tgt_ncn",
									tAaRegPrcNcnMgtDto
											.getLong("unc_irt_tgt_ncn") + 1);
						} else {

							// =============================================================================
							// ######### GeneralCodeBlock ##생략건수 + 1
							// =============================================================================
							tAaRegTgtListInqDto.setString("omss_yn", "Y");
							tAaRegPrcNcnMgtDto.setLong("omss_ncn",
									tAaRegPrcNcnMgtDto.getLong("omss_ncn") + 1);

							// =============================================================================
							// ######### GeneralCodeBlock ##continue
							// =============================================================================
							continue;
						}
					}
				}
			} else {

				// =============================================================================
				// ######### GeneralCodeBlock ##생략건수 + 1
				// =============================================================================
				tAaRegTgtListInqDto.setString("omss_yn", "Y");
				tAaRegPrcNcnMgtDto.setLong("omss_ncn",
						tAaRegPrcNcnMgtDto.getLong("omss_ncn") + 1);

				// =============================================================================
				// ######### GeneralCodeBlock ##continue
				// =============================================================================
				continue;
			}
			if (KLDataConvertUtil.notEquals("Y",
					tAaRegTgtListInqDto.getString("omss_yn"))) {

				//// 4. 연체작업의 현 연체상태 확인 및 DPD, PPD 계산
				{

					// =============================================================================
					// ######### GeneralCodeBlock ##사후연체기본연체상태조회  입력값 세팅
					// =============================================================================
					// [i사후연체기본연체상태조회]  

					iAffcAaBasAaSsInq.setString("ln_no",
							tAaRegTgtListInqDto.getString("ln_no"));
					iAffcAaBasAaSsInq.setLong("ln_seq",
							tAaRegTgtListInqDto.getLong("ln_seq"));
					commonDao = new LCommonDao(
							"batch/lon/rl/job/aamgt/aaregjob/AaReg/retvAffcAaBasAaSs",
							iAffcAaBasAaSsInq); //##사후연체기본연체상태조회
					rAffcAaBasAaSsInq = commonDao.executeQueryOnlySingle();

					// =============================================================================
					// ######### GeneralCodeBlock ##사후연체기본연체상태조회  결과값 맵핑
					// =============================================================================
					// [r사후연체기본연체상태조회]

					tAaRegTgtListInqDto.setString("aa_dv_cd",
							rAffcAaBasAaSsInq.getString("aa_dv_cd"));
					if (KLDataConvertUtil.equals("1",
							rAffcAaBasAaSsInq.getString("aa_dv_cd"))) {

						// =============================================================================
						// ######### GeneralCodeBlock ##연체발생일자 셋팅
						// =============================================================================
						tAaRegTgtListInqDto.setString("aa_occ_dt",
								rAffcAaBasAaSsInq.getString("aa_occ_dt"));
						tAaRegTgtListInqDto.setString("aa_occ_bs_dt",
								rAffcAaBasAaSsInq.getString("aa_occ_bs_dt"));

						LLog.debug
								.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
						LLog.debug.println("<< PPD / DPD 계산기준일자 >>");
						LLog.debug.println("연체발생일자     : "
								+ tAaRegTgtListInqDto.getString("aa_occ_dt"));
						LLog.debug
								.println("연체발생기준일자 : "
										+ tAaRegTgtListInqDto
												.getString("aa_occ_bs_dt"));
						LLog.debug
								.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
					}

					//// DPD 계산
					{
						try {

							// =============================================================================
							// ######### GeneralCodeBlock ##DPD계산
							// =============================================================================
							int iAaDcn = KDateUtil.getDayInterval(
									tAaRegTgtListInqDto.getString("aa_occ_dt"),
									mtAaClcBsDt); //#i연체일수

							if (iAaDcn >= 0) {
								if (KLDataConvertUtil.equals(iAaDcn, 0)) {
									tAaRegTgtListInqDto.setLong("aa_dcn", 1); //FROM , TO 날짜가 같을때는 1일로..
								} else {
									tAaRegTgtListInqDto.setLong("aa_dcn",
											iAaDcn);
								}
							}
							if (iAaDcn < 0) {

								// =============================================================================
								// ######### GeneralCodeBlock ##write  입력값 세팅 (DPD COMP ERR / (-)발생)
								// =============================================================================
								// [i사후연체기본연체등록]

								iAffcAaBasAaReg = "<< DPD COMP ERR / (-)발생 >>   "
										+ "대출번호 : "
										+ tAaRegTgtListInqDto
												.getString("ln_no")
										+ ", "
										+ "대출일련번호 : "
										+ tAaRegTgtListInqDto.getLong("ln_seq")
										+ ", "
										+ "연체발생일자 : "
										+ tAaRegTgtListInqDto
												.getString("aa_occ_dt")
										+ ", "
										+ "연체발생기준일자 : "
										+ tAaRegTgtListInqDto
												.getString("aa_occ_bs_dt")
										+ ", "
										+ "연체계산기준일자 : "
										+ mtAaClcBsDt
										+ ", " + "계산된 DPD : " + iAaDcn + "\n\n";
								writer.writeBytes(iAffcAaBasAaReg);

								// =============================================================================
								// ######### GeneralCodeBlock ##오류건수 + 1
								// =============================================================================
								tAaRegTgtListInqDto.setString("omss_yn", "Y");
								tAaRegPrcNcnMgtDto
										.setLong("err_ncn", tAaRegPrcNcnMgtDto
												.getLong("err_ncn") + 1);

								// =============================================================================
								// ######### GeneralCodeBlock ##continue
								// =============================================================================
								continue;
							}
						} catch (Exception e) {

							// =============================================================================
							// ######### GeneralCodeBlock ##write  입력값 세팅 (DPD 계산중 오류발생)
							// =============================================================================
							// [i사후연체기본연체등록]  

							iAffcAaBasAaReg = "<< DPD 계산중 오류발생 >>   "
									+ "대출번호 : "
									+ tAaRegTgtListInqDto.getString("ln_no")
									+ ", " + "대출일련번호 : "
									+ tAaRegTgtListInqDto.getLong("ln_seq")
									+ "\n " + "  --> 오류메세지 : "
									+ KExceptionUtil.getMessage(e) + "\n\n";
							writer.writeBytes(iAffcAaBasAaReg);

							// =============================================================================
							// ######### GeneralCodeBlock ##오류건수 + 1
							// =============================================================================
							tAaRegTgtListInqDto.setString("omss_yn", "Y");
							tAaRegPrcNcnMgtDto.setLong("err_ncn",
									tAaRegPrcNcnMgtDto.getLong("err_ncn") + 1);

							// =============================================================================
							// ######### GeneralCodeBlock ##continue
							// =============================================================================
							continue;
						}
					}

					//// PPD 계산
					{
						try {

							// =============================================================================
							// ######### GeneralCodeBlock ##PPD, PPD2 계산
							// =============================================================================
							tAaRegTgtListInqDto
									.setLong(
											"aa_tim",
											(KTypeConverter.parseTo_int(KDateUtil
													.getDate(mtAaClcBsDt,
															"yyyy")) - KTypeConverter.parseTo_int(KDateUtil.getDate(
													tAaRegTgtListInqDto
															.getString("aa_occ_bs_dt"),
													"yyyy")))
													* 12
													+ (KTypeConverter
															.parseTo_int(KDateUtil
																	.getDate(
																			mtAaClcBsDt,
																			"MM")) - KTypeConverter
															.parseTo_int(KDateUtil.getDate(
																	tAaRegTgtListInqDto
																			.getString("aa_occ_bs_dt"),
																	"MM"))) + 1);

							tAaRegTgtListInqDto
									.setLong(
											"aa_tim2",
											(KTypeConverter
													.parseTo_int(KDateUtil
															.getDate(mtBsDt,
																	"yyyy")) - KTypeConverter.parseTo_int(KDateUtil.getDate(
													tAaRegTgtListInqDto
															.getString("aa_occ_bs_dt"),
													"yyyy")))
													* 12
													+ (KTypeConverter
															.parseTo_int(KDateUtil
																	.getDate(
																			mtBsDt,
																			"MM")) - KTypeConverter
															.parseTo_int(KDateUtil.getDate(
																	tAaRegTgtListInqDto
																			.getString("aa_occ_bs_dt"),
																	"MM"))) + 1);
							if (tAaRegTgtListInqDto.getLong("aa_tim") < 0
									|| tAaRegTgtListInqDto.getLong("aa_tim2") < 0) {

								// =============================================================================
								// ######### GeneralCodeBlock ##write  입력값 세팅 (PPD COMP ERR / (-)발생)
								// =============================================================================
								// [i사후연체기본연체등록]

								iAffcAaBasAaReg = "<< PPD COMP ERR / (-)발생 >>   "
										+ "대출번호 : "
										+ tAaRegTgtListInqDto
												.getString("ln_no")
										+ ", "
										+ "대출일련번호 : "
										+ tAaRegTgtListInqDto.getLong("ln_seq")
										+ ", "
										+ "연체발생일자 : "
										+ tAaRegTgtListInqDto
												.getString("aa_occ_dt")
										+ ", "
										+ "연체발생기준일자 : "
										+ tAaRegTgtListInqDto
												.getString("aa_occ_bs_dt")
										+ ", "
										+ "연체계산기준일자 : "
										+ mtAaClcBsDt
										+ ", "
										+ "계산된 PPD : "
										+ tAaRegTgtListInqDto.getLong("aa_tim")
										+ ", "
										+ "계산된 PPD2 : "
										+ tAaRegTgtListInqDto
												.getLong("aa_tim2") + "\n\n";
								writer.writeBytes(iAffcAaBasAaReg);

								// =============================================================================
								// ######### GeneralCodeBlock ##오류건수 + 1
								// =============================================================================
								tAaRegTgtListInqDto.setString("omss_yn", "Y");
								tAaRegPrcNcnMgtDto
										.setLong("err_ncn", tAaRegPrcNcnMgtDto
												.getLong("err_ncn") + 1);

								// =============================================================================
								// ######### GeneralCodeBlock ##continue
								// =============================================================================
								continue;
							}
						} catch (Exception e) {

							// =============================================================================
							// ######### GeneralCodeBlock ##write  입력값 세팅 (PPD 계산중 오류발생)
							// =============================================================================
							// [i사후연체기본연체등록]  
							iAffcAaBasAaReg = "<< PPD 계산중 오류발생 >>   "
									+ "대출번호 : "
									+ tAaRegTgtListInqDto.getString("ln_no")
									+ ", " + "대출일련번호 : "
									+ tAaRegTgtListInqDto.getLong("ln_seq")
									+ "\n " + "  --> 오류메세지 : "
									+ KExceptionUtil.getMessage(e) + "\n\n";
							writer.writeBytes(iAffcAaBasAaReg);

							// =============================================================================
							// ######### GeneralCodeBlock ##오류건수 + 1
							// =============================================================================
							tAaRegTgtListInqDto.setString("omss_yn", "Y");
							tAaRegPrcNcnMgtDto.setLong("err_ncn",
									tAaRegPrcNcnMgtDto.getLong("err_ncn") + 1);

							// =============================================================================
							// ######### GeneralCodeBlock ##continue
							// =============================================================================
							continue;
						}
					}
				}
			}
			if (KLDataConvertUtil.notEquals("Y",
					tAaRegTgtListInqDto.getString("omss_yn"))) {

				//// 5. 연체상태에 따른 연체기본 TABLE 처리
				{
					context.setTotalProgress(this,
							context.getTotalProgress(this) + 1); //##setTotalCnt

					// =============================================================================
					// ######### GeneralCodeBlock ##연체대상건수 + 1
					// =============================================================================
					tAaRegPrcNcnMgtDto.setLong("aa_tgt_ncn",
							tAaRegPrcNcnMgtDto.getLong("aa_tgt_ncn") + 1);
					try {
						if (KLDataConvertUtil.equals("0",
								tAaRegTgtListInqDto.getString("aa_dv_cd")) // 연체구분코드(0:연체해제상태)
						) {
							if (mtHldyYn) {

								// =============================================================================
								// ######### GeneralCodeBlock ##비영업일처리건수 + 1
								// =============================================================================
								tAaRegPrcNcnMgtDto
										.setLong(
												"nbz_day_prc_ncn",
												tAaRegPrcNcnMgtDto
														.getLong("nbz_day_prc_ncn") + 1);

								// =============================================================================
								// ######### GeneralCodeBlock ##continue
								// =============================================================================
								continue;
							} else {

								// =============================================================================
								// ######### GeneralCodeBlock ##사후연체기본[연체해제상태]수정  입력값 세팅
								// =============================================================================
								// [i사후연체기본연체해제상태수정]  

								iAffcAaBasAaRelsSsUpd = KCommMngUtil.initLData(
										iAffcAaBasAaRelsSsUpd, new String[] {
												"ln_no", "ln_seq",
												"bd_aa_ss_cd",
												"lon_pdt_lcl_cd",
												"lon_pdt_mcl_cd",
												"lon_pdt_smcl_cd", "aa_dcn",
												"accu_aa_dcn", "aa_yn",
												"aa_rels_dt", "aa_occ_dt",
												"aa_occ_bs_dt", "aa_reg_dt",
												"aa_reg_rsn_cd",
												"aa_rels_rsn_cd", "aa_tim",
												"aa_tim2", "exp_dt",
												"pa_fsr_dt", "cmpe_ls_dt",
												"nt_rpy_dt",
												"aa_crat_pttm_ln_amt",
												"aa_crat_pttm_ln_bal",
												"aa_reg_om_no", "jrsd_orz_cd",
												"del_yn", "aa_mgt_orz_dv_cd",
												"aa_mgt_orz_dist_dt",
												"aa_mgt_orz_cd",
												"aa_mgt_om_no",
												"aa_mgt_om_dist_dt", "csg_dt",
												"cscm_dept_nm",
												"csg_bd_chp_nm",
												"csg_bd_chp_tlno", "fs_prr_dt",
												"ch_prr_dt", "sms_snd_dt",
												"sms_snd_dv_cd", "rqe_orz_cd",
												"rqe_om_no", "msk_cm_ss_cd",
												"adn_cd", "adn_snd_dt",
												"adn_rr_yn", "ib_ob_dv_cd",
												"rem_prms_dt", "rem_prms_tm",
												"rge_prg_dt", "rge_prg_tm",
												"ls_csl_tp_cd", "ls_csl_om_no",
												"ls_csl_dt", "ls_csl_tm",
												"erpd_alm_bs_ym",
												"erd_aa_ge_scr",
												"erd_aa_ge_gd_cd",
												"erpd_alm_sgn_val",
												"erpd_alm_gd_cd",
												"erpd_alm_scr", "egd_rsn_cd",
												"fnn_cer_dctd_yn",
												"lsdb_cap_ln_acc_eyn",
												"fs_reg_dt", "prc_ip_adr",
												"prc_prog_id", "fs_in_usr_id",
												"fs_in_dtm", "ls_ch_usr_id",
												"ls_ch_dtm" }, new String[] {
												"STRING", "LONG", "STRING",
												"STRING", "STRING", "STRING",
												"LONG", "LONG", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"LONG", "LONG", "STRING",
												"STRING", "STRING", "STRING",
												"BIGDECIMAL", "BIGDECIMAL",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "LONG",
												"STRING", "LONG", "STRING",
												"LONG", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING" }, true);
								iAffcAaBasAaRelsSsUpd.setString("ln_no",
										tAaRegTgtListInqDto.getString("ln_no"));
								iAffcAaBasAaRelsSsUpd.setLong("ln_seq",
										tAaRegTgtListInqDto.getLong("ln_seq"));

								if (KLDataConvertUtil.equals("Y",
										tAaRegTgtListInqDto
												.getString("spc_bd_reg_yn"))) {
									iAffcAaBasAaRelsSsUpd.setString(
											"bd_aa_ss_cd", "20");
								} else {
									iAffcAaBasAaRelsSsUpd.setString(
											"bd_aa_ss_cd", "10");
								}
								iAffcAaBasAaRelsSsUpd.setString(
										"lon_pdt_lcl_cd", tAaRegTgtListInqDto
												.getString("lon_pdt_lcl_cd"));
								iAffcAaBasAaRelsSsUpd.setString(
										"lon_pdt_mcl_cd", tAaRegTgtListInqDto
												.getString("lon_pdt_mcl_cd"));
								iAffcAaBasAaRelsSsUpd.setString(
										"lon_pdt_smcl_cd", tAaRegTgtListInqDto
												.getString("lon_pdt_smcl_cd"));
								iAffcAaBasAaRelsSsUpd.setLong("aa_dcn",
										tAaRegTgtListInqDto.getLong("aa_dcn"));
								iAffcAaBasAaRelsSsUpd.setString("aa_yn", "Y");
								iAffcAaBasAaRelsSsUpd.setString("aa_rels_dt",
										"00010101");
								iAffcAaBasAaRelsSsUpd.setString("aa_occ_dt",
										tAaRegTgtListInqDto
												.getString("aa_occ_dt"));
								iAffcAaBasAaRelsSsUpd.setString("aa_occ_bs_dt",
										tAaRegTgtListInqDto
												.getString("aa_occ_bs_dt"));
								iAffcAaBasAaRelsSsUpd.setString("aa_reg_dt",
										mtBsDt);
								iAffcAaBasAaRelsSsUpd.setString(
										"aa_reg_rsn_cd", tAaRegTgtListInqDto
												.getString("aa_reg_rsn_cd"));
								iAffcAaBasAaRelsSsUpd.setString(
										"aa_rels_rsn_cd", "");
								iAffcAaBasAaRelsSsUpd.setLong("aa_tim",
										tAaRegTgtListInqDto.getLong("aa_tim"));
								iAffcAaBasAaRelsSsUpd.setLong("aa_tim2",
										tAaRegTgtListInqDto.getLong("aa_tim2"));
								iAffcAaBasAaRelsSsUpd
										.setString("exp_dt",
												tAaRegTgtListInqDto
														.getString("exp_dt"));
								iAffcAaBasAaRelsSsUpd.setString("pa_fsr_dt",
										tAaRegTgtListInqDto
												.getString("pa_fsr_dt"));
								iAffcAaBasAaRelsSsUpd.setString("cmpe_ls_dt",
										tAaRegTgtListInqDto
												.getString("cmpe_ls_dt"));
								iAffcAaBasAaRelsSsUpd.setString("nt_rpy_dt",
										tAaRegTgtListInqDto
												.getString("nt_rpy_dt"));
								iAffcAaBasAaRelsSsUpd.setBigDecimal(
										"aa_crat_pttm_ln_amt",
										tAaRegTgtListInqDto
												.getBigDecimal("ln_amt"));
								iAffcAaBasAaRelsSsUpd.setBigDecimal(
										"aa_crat_pttm_ln_bal",
										tAaRegTgtListInqDto
												.getBigDecimal("ln_bal"));
								iAffcAaBasAaRelsSsUpd.setString("aa_reg_om_no",
										"SYSTEM");
								iAffcAaBasAaRelsSsUpd.setString("jrsd_orz_cd",
										tAaRegTgtListInqDto
												.getString("jrsd_orz_cd"));
								iAffcAaBasAaRelsSsUpd.setString("del_yn", "N");
								commonDao = new LCommonDao(
										"batch/lon/rl/job/aamgt/aaregjob/AaReg/uptAffcAaBasByAaRelsSs",
										iAffcAaBasAaRelsSsUpd); //##사후연체기본[연체해제상태]수정
								rAffcAaBasAaRelsSsUpd = commonDao
										.executeUpdate();
								if (KLDataConvertUtil.equals(1,
										rAffcAaBasAaRelsSsUpd) // 수정 정상처리시..
								) {

									// =============================================================================
									// ######### GeneralCodeBlock ##현재연체해제건수 + 1
									// =============================================================================
									tAaRegPrcNcnMgtDto
											.setLong(
													"now_aa_rels_ncn",
													tAaRegPrcNcnMgtDto
															.getLong("now_aa_rels_ncn") + 1);

									//// 고객접촉내역 등록
									{

										// =============================================================================
										// ######### GeneralCodeBlock ##고객접촉내역등록  입력값 세팅 (연체등록)
										// =============================================================================
										// [ic고객접촉내역등록]

										// [t연체등록대상목록조회Dto]

										icCsCntcHisReg.setString("ig_cs_no",
												tAaRegTgtListInqDto
														.getString("ig_cs_no"));
										icCsCntcHisReg
												.setString(
														"cntc_dtm",
														KDateUtil
																.getCurrentDate("yyyyMMdd"));
										icCsCntcHisReg.setString("cntc_sys_cd",
												"LON"); // LON    : 여신
										icCsCntcHisReg.setString("cntc_lcl_cd",
												"Z002"); // Z002   : 유지
										icCsCntcHisReg.setString("cntc_mcl_cd",
												"Z0022"); // Z0022  : 대출상태변경
										icCsCntcHisReg.setString(
												"cntc_smcl_cd", "Z00043"); // Z00043 : 연체발생
										icCsCntcHisReg.setString(
												"req_cs_rlt_cd", "01"); // 01     : 본인
										icCsCntcHisReg.setString(
												"cntc_infl_pth_dv_cd", "01"); // 01     : 사무계
										icCsCntcHisReg.setString("prc_orz_cd",
												BchCntcPrcOrzImConst.ORZ_CD
														.getCode());
										icCsCntcHisReg.setString("prc_om_no",
												BchCntcPrcOrzImConst.OM_CD
														.getCode());

										icCsCntcHisReg
												.setString(
														"cntc_dtl_txt",
														"연체등록 (연체발생일자 : "
																+ tAaRegTgtListInqDto
																		.getString("aa_occ_dt")
																+ ", 연체등록사유 : "
																+ KIntegrationCodeUtil
																		.getIgCdValiValNm(
																				"AA_REG_RSN_CD",
																				tAaRegTgtListInqDto
																						.getString("aa_reg_rsn_cd"))
																+ ")");
										LProtocolInitializeUtil
												.primitiveLMultiInitialize(icCsCntcHisReg);
										rcCsCntcHisReg = igCsCntcImIbc
												.regtCsCntcHis(icCsCntcHisReg); //##고객접촉내역등록
									}
								} else {

									// =============================================================================
									// ######### GeneralCodeBlock ##write  입력값 세팅 << 연체해제상태 UPDATE ERROR >>
									// =============================================================================
									// [i사후연체기본연체등록]

									iAffcAaBasAaReg = "<< 연체해제상태 UPDATE ERROR >>   "
											+ "대출번호 : "
											+ tAaRegTgtListInqDto
													.getString("ln_no")
											+ ", "
											+ "대출일련번호 : "
											+ tAaRegTgtListInqDto
													.getLong("ln_seq") + "\n\n";
									writer.writeBytes(iAffcAaBasAaReg);
									context.addErrorCount(this, 1); //##setErrorCnt

									// =============================================================================
									// ######### GeneralCodeBlock ##오류건수 + 1
									// =============================================================================
									tAaRegTgtListInqDto.setString("omss_yn",
											"Y");
									tAaRegPrcNcnMgtDto.setLong("err_ncn",
											tAaRegPrcNcnMgtDto
													.getLong("err_ncn") + 1);

									// =============================================================================
									// ######### GeneralCodeBlock ##continue
									// =============================================================================
									continue;
								}
							}
						} else if (KLDataConvertUtil.equals("1",
								tAaRegTgtListInqDto.getString("aa_dv_cd")) // 연체구분코드(1:연체상태)
						) {

							// =============================================================================
							// ######### GeneralCodeBlock ##사후연체기본[연체상태]수정  입력값 세팅
							// =============================================================================
							// [i사후연체기본연체상태수정]

							iAffcAaBasAaSsUpd = KCommMngUtil.initLData(
									iAffcAaBasAaSsUpd, new String[] { "ln_no",
											"ln_seq", "bd_aa_ss_cd",
											"lon_pdt_lcl_cd", "lon_pdt_mcl_cd",
											"lon_pdt_smcl_cd", "aa_dcn",
											"accu_aa_dcn", "aa_yn",
											"aa_rels_dt", "aa_occ_dt",
											"aa_occ_bs_dt", "aa_reg_dt",
											"aa_reg_rsn_cd", "aa_rels_rsn_cd",
											"aa_tim", "aa_tim2", "exp_dt",
											"pa_fsr_dt", "cmpe_ls_dt",
											"nt_rpy_dt", "aa_crat_pttm_ln_amt",
											"aa_crat_pttm_ln_bal",
											"aa_reg_om_no", "jrsd_orz_cd",
											"del_yn", "aa_mgt_orz_dv_cd",
											"aa_mgt_orz_dist_dt",
											"aa_mgt_orz_cd", "aa_mgt_om_no",
											"aa_mgt_om_dist_dt", "csg_dt",
											"cscm_dept_nm", "csg_bd_chp_nm",
											"csg_bd_chp_tlno", "fs_prr_dt",
											"ch_prr_dt", "sms_snd_dt",
											"sms_snd_dv_cd", "rqe_orz_cd",
											"rqe_om_no", "msk_cm_ss_cd",
											"adn_cd", "adn_snd_dt",
											"adn_rr_yn", "ib_ob_dv_cd",
											"rem_prms_dt", "rem_prms_tm",
											"rge_prg_dt", "rge_prg_tm",
											"ls_csl_tp_cd", "ls_csl_om_no",
											"ls_csl_dt", "ls_csl_tm",
											"erpd_alm_bs_ym", "erd_aa_ge_scr",
											"erd_aa_ge_gd_cd",
											"erpd_alm_sgn_val",
											"erpd_alm_gd_cd", "erpd_alm_scr",
											"egd_rsn_cd", "fnn_cer_dctd_yn",
											"lsdb_cap_ln_acc_eyn", "fs_reg_dt",
											"prc_ip_adr", "prc_prog_id",
											"fs_in_usr_id", "fs_in_dtm",
											"ls_ch_usr_id", "ls_ch_dtm" },
									new String[] { "STRING", "LONG", "STRING",
											"STRING", "STRING", "STRING",
											"LONG", "LONG", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "LONG", "LONG",
											"STRING", "STRING", "STRING",
											"STRING", "BIGDECIMAL",
											"BIGDECIMAL", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"LONG", "STRING", "LONG", "STRING",
											"LONG", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING" }, true);
							iAffcAaBasAaSsUpd.setString("ln_no",
									tAaRegTgtListInqDto.getString("ln_no"));
							iAffcAaBasAaSsUpd.setLong("ln_seq",
									tAaRegTgtListInqDto.getLong("ln_seq"));

							if (KLDataConvertUtil.equals("Y",
									tAaRegTgtListInqDto
											.getString("spc_bd_reg_yn"))) {
								iAffcAaBasAaSsUpd
										.setString("bd_aa_ss_cd", "20");
							} else {
								iAffcAaBasAaSsUpd
										.setString("bd_aa_ss_cd", "10");
							}
							iAffcAaBasAaSsUpd.setString("lon_pdt_lcl_cd",
									tAaRegTgtListInqDto
											.getString("lon_pdt_lcl_cd"));
							iAffcAaBasAaSsUpd.setString("lon_pdt_mcl_cd",
									tAaRegTgtListInqDto
											.getString("lon_pdt_mcl_cd"));
							iAffcAaBasAaSsUpd.setString("lon_pdt_smcl_cd",
									tAaRegTgtListInqDto
											.getString("lon_pdt_smcl_cd"));
							iAffcAaBasAaSsUpd.setLong("aa_dcn",
									tAaRegTgtListInqDto.getLong("aa_dcn"));
							iAffcAaBasAaSsUpd.setString("aa_yn", "Y");
							iAffcAaBasAaSsUpd.setString("aa_rels_dt",
									"00010101");
							iAffcAaBasAaSsUpd.setString("aa_occ_dt",
									tAaRegTgtListInqDto.getString("aa_occ_dt"));
							iAffcAaBasAaSsUpd.setString("aa_rels_rsn_cd", "");
							iAffcAaBasAaSsUpd.setLong("aa_tim",
									tAaRegTgtListInqDto.getLong("aa_tim"));
							iAffcAaBasAaSsUpd.setLong("aa_tim2",
									tAaRegTgtListInqDto.getLong("aa_tim2"));
							iAffcAaBasAaSsUpd.setString("exp_dt",
									tAaRegTgtListInqDto.getString("exp_dt"));
							iAffcAaBasAaSsUpd.setString("pa_fsr_dt",
									tAaRegTgtListInqDto.getString("pa_fsr_dt"));
							iAffcAaBasAaSsUpd
									.setString("cmpe_ls_dt",
											tAaRegTgtListInqDto
													.getString("cmpe_ls_dt"));
							iAffcAaBasAaSsUpd.setString("nt_rpy_dt",
									tAaRegTgtListInqDto.getString("nt_rpy_dt"));
							iAffcAaBasAaSsUpd
									.setBigDecimal("aa_crat_pttm_ln_amt",
											tAaRegTgtListInqDto
													.getBigDecimal("ln_amt"));
							iAffcAaBasAaSsUpd
									.setBigDecimal("aa_crat_pttm_ln_bal",
											tAaRegTgtListInqDto
													.getBigDecimal("ln_bal"));
							iAffcAaBasAaSsUpd.setString("jrsd_orz_cd",
									tAaRegTgtListInqDto
											.getString("jrsd_orz_cd"));
							iAffcAaBasAaSsUpd.setString("del_yn", "N");
							commonDao = new LCommonDao(
									"batch/lon/rl/job/aamgt/aaregjob/AaReg/uptAffcAaBasByAaSs",
									iAffcAaBasAaSsUpd); //##사후연체기본[연체상태]수정
							rAffcAaBasAaSsUpd = commonDao.executeUpdate();
							if (KLDataConvertUtil.equals(1, rAffcAaBasAaSsUpd) // 수정 정상처리시..
							) {

								// =============================================================================
								// ######### GeneralCodeBlock ##현재연체건수 + 1
								// =============================================================================
								tAaRegPrcNcnMgtDto.setLong("now_aa_ncn",
										tAaRegPrcNcnMgtDto
												.getLong("now_aa_ncn") + 1);
							} else {

								// =============================================================================
								// ######### GeneralCodeBlock ##write  입력값 세팅 << 연체상태 UPDATE ERROR >>
								// =============================================================================
								// [i사후연체기본연체등록]  

								iAffcAaBasAaReg = "<< 연체상태 UPDATE ERROR >>   "
										+ "대출번호 : "
										+ tAaRegTgtListInqDto
												.getString("ln_no") + ", "
										+ "대출일련번호 : "
										+ tAaRegTgtListInqDto.getLong("ln_seq")
										+ "\n\n";
								writer.writeBytes(iAffcAaBasAaReg);
								context.addErrorCount(this, 1); //##setErrorCnt

								// =============================================================================
								// ######### GeneralCodeBlock ##오류건수 + 1
								// =============================================================================
								tAaRegTgtListInqDto.setString("omss_yn", "Y");
								tAaRegPrcNcnMgtDto
										.setLong("err_ncn", tAaRegPrcNcnMgtDto
												.getLong("err_ncn") + 1);

								// =============================================================================
								// ######### GeneralCodeBlock ##continue
								// =============================================================================
								continue;
							}
						} else if (KLDataConvertUtil.equals("2",
								tAaRegTgtListInqDto.getString("aa_dv_cd")) // 연체구분코드(2:신규연체상태)
						) {
							if (mtHldyYn) {

								// =============================================================================
								// ######### GeneralCodeBlock ##비영업일처리건수 + 1
								// =============================================================================
								tAaRegPrcNcnMgtDto
										.setLong(
												"nbz_day_prc_ncn",
												tAaRegPrcNcnMgtDto
														.getLong("nbz_day_prc_ncn") + 1);

								// =============================================================================
								// ######### GeneralCodeBlock ##continue
								// =============================================================================
								continue;
							} else {

								// =============================================================================
								// ######### GeneralCodeBlock ##사후연체기본[신규연체]등록  입력값 세팅
								// =============================================================================
								// [i사후연체기본신규연체등록]  

								iAffcAaBasNwAaReg = KCommMngUtil.initLData(
										iAffcAaBasNwAaReg, new String[] {
												"ln_no", "ln_seq",
												"bd_aa_ss_cd",
												"lon_pdt_lcl_cd",
												"lon_pdt_mcl_cd",
												"lon_pdt_smcl_cd", "aa_dcn",
												"accu_aa_dcn", "aa_yn",
												"aa_rels_dt", "aa_occ_dt",
												"aa_occ_bs_dt", "aa_reg_dt",
												"aa_reg_rsn_cd",
												"aa_rels_rsn_cd", "aa_tim",
												"aa_tim2", "exp_dt",
												"pa_fsr_dt", "cmpe_ls_dt",
												"nt_rpy_dt",
												"aa_crat_pttm_ln_amt",
												"aa_crat_pttm_ln_bal",
												"aa_reg_om_no", "jrsd_orz_cd",
												"del_yn", "aa_mgt_orz_dv_cd",
												"aa_mgt_orz_dist_dt",
												"aa_mgt_orz_cd",
												"aa_mgt_om_no",
												"aa_mgt_om_dist_dt", "csg_dt",
												"cscm_dept_nm",
												"csg_bd_chp_nm",
												"csg_bd_chp_tlno", "fs_prr_dt",
												"ch_prr_dt", "sms_snd_dt",
												"sms_snd_dv_cd", "rqe_orz_cd",
												"rqe_om_no", "msk_cm_ss_cd",
												"adn_cd", "adn_snd_dt",
												"adn_rr_yn", "ib_ob_dv_cd",
												"rem_prms_dt", "rem_prms_tm",
												"rge_prg_dt", "rge_prg_tm",
												"ls_csl_tp_cd", "ls_csl_om_no",
												"ls_csl_dt", "ls_csl_tm",
												"erpd_alm_bs_ym",
												"erd_aa_ge_scr",
												"erd_aa_ge_gd_cd",
												"erpd_alm_sgn_val",
												"erpd_alm_gd_cd",
												"erpd_alm_scr", "egd_rsn_cd",
												"fnn_cer_dctd_yn",
												"lsdb_cap_ln_acc_eyn",
												"fs_reg_dt", "prc_ip_adr",
												"prc_prog_id", "fs_in_usr_id",
												"fs_in_dtm", "ls_ch_usr_id",
												"ls_ch_dtm" }, new String[] {
												"STRING", "LONG", "STRING",
												"STRING", "STRING", "STRING",
												"LONG", "LONG", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"LONG", "LONG", "STRING",
												"STRING", "STRING", "STRING",
												"BIGDECIMAL", "BIGDECIMAL",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "LONG",
												"STRING", "LONG", "STRING",
												"LONG", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING", "STRING",
												"STRING", "STRING" }, true);
								iAffcAaBasNwAaReg.setString("ln_no",
										tAaRegTgtListInqDto.getString("ln_no"));
								iAffcAaBasNwAaReg.setLong("ln_seq",
										tAaRegTgtListInqDto.getLong("ln_seq"));

								if (KLDataConvertUtil.equals("Y",
										tAaRegTgtListInqDto
												.getString("spc_bd_reg_yn"))) {
									iAffcAaBasNwAaReg.setString("bd_aa_ss_cd",
											"20");
								} else {
									iAffcAaBasNwAaReg.setString("bd_aa_ss_cd",
											"10");
								}
								iAffcAaBasNwAaReg.setString("lon_pdt_lcl_cd",
										tAaRegTgtListInqDto
												.getString("lon_pdt_lcl_cd"));
								iAffcAaBasNwAaReg.setString("lon_pdt_mcl_cd",
										tAaRegTgtListInqDto
												.getString("lon_pdt_mcl_cd"));
								iAffcAaBasNwAaReg.setString("lon_pdt_smcl_cd",
										tAaRegTgtListInqDto
												.getString("lon_pdt_smcl_cd"));
								iAffcAaBasNwAaReg.setLong("aa_dcn",
										tAaRegTgtListInqDto.getLong("aa_dcn"));
								iAffcAaBasNwAaReg.setLong("accu_aa_dcn",
										tAaRegTgtListInqDto.getLong("aa_dcn"));
								iAffcAaBasNwAaReg.setString("aa_yn", "Y");
								iAffcAaBasNwAaReg.setString("aa_rels_dt",
										"00010101");
								iAffcAaBasNwAaReg.setString("aa_occ_dt",
										tAaRegTgtListInqDto
												.getString("aa_occ_dt"));
								iAffcAaBasNwAaReg.setString("aa_occ_bs_dt",
										tAaRegTgtListInqDto
												.getString("aa_occ_bs_dt"));
								iAffcAaBasNwAaReg
										.setString("aa_reg_dt", mtBsDt);
								iAffcAaBasNwAaReg.setString("aa_reg_rsn_cd",
										tAaRegTgtListInqDto
												.getString("aa_reg_rsn_cd"));
								iAffcAaBasNwAaReg.setString("aa_rels_rsn_cd",
										"");
								iAffcAaBasNwAaReg.setLong("aa_tim",
										tAaRegTgtListInqDto.getLong("aa_tim"));
								iAffcAaBasNwAaReg.setLong("aa_tim2",
										tAaRegTgtListInqDto.getLong("aa_tim2"));
								iAffcAaBasNwAaReg
										.setString("exp_dt",
												tAaRegTgtListInqDto
														.getString("exp_dt"));
								iAffcAaBasNwAaReg.setString("pa_fsr_dt",
										tAaRegTgtListInqDto
												.getString("pa_fsr_dt"));
								iAffcAaBasNwAaReg.setString("cmpe_ls_dt",
										tAaRegTgtListInqDto
												.getString("cmpe_ls_dt"));
								iAffcAaBasNwAaReg.setString("nt_rpy_dt",
										tAaRegTgtListInqDto
												.getString("nt_rpy_dt"));
								iAffcAaBasNwAaReg.setBigDecimal(
										"aa_crat_pttm_ln_amt",
										tAaRegTgtListInqDto
												.getBigDecimal("ln_amt"));
								iAffcAaBasNwAaReg.setBigDecimal(
										"aa_crat_pttm_ln_bal",
										tAaRegTgtListInqDto
												.getBigDecimal("ln_bal"));
								iAffcAaBasNwAaReg.setString("aa_reg_om_no",
										"SYSTEM");
								iAffcAaBasNwAaReg.setString("jrsd_orz_cd",
										tAaRegTgtListInqDto
												.getString("jrsd_orz_cd"));
								iAffcAaBasNwAaReg.setString("del_yn", "N");
								iAffcAaBasNwAaReg.setString("fs_reg_dt",
										KDateUtil.getCurrentDate("yyyyMMdd"));
								commonDao = new LCommonDao(
										"batch/lon/rl/job/aamgt/aaregjob/AaReg/regtAffcAaBasByNwAa",
										iAffcAaBasNwAaReg); //##사후연체기본[신규연체]등록
								rAffcAaBasNwAaReg = commonDao.executeUpdate();
								if (KLDataConvertUtil.equals(1,
										rAffcAaBasNwAaReg) // 등록 정상처리시..
								) {

									// =============================================================================
									// ######### GeneralCodeBlock ##신규연체건수 + 1
									// =============================================================================
									tAaRegPrcNcnMgtDto.setLong("nw_aa_ncn",
											tAaRegPrcNcnMgtDto
													.getLong("nw_aa_ncn") + 1);

									//// 고객접촉내역 등록
									{

										// =============================================================================
										// ######### GeneralCodeBlock ##고객접촉내역등록  입력값 세팅 (연체등록)
										// =============================================================================
										// [ic고객접촉내역등록]  

										icCsCntcHisReg.setString("ig_cs_no",
												tAaRegTgtListInqDto
														.getString("ig_cs_no"));
										icCsCntcHisReg
												.setString(
														"cntc_dtm",
														KDateUtil
																.getCurrentDate("yyyyMMdd"));
										icCsCntcHisReg.setString("cntc_sys_cd",
												"LON"); // LON    : 여신
										icCsCntcHisReg.setString("cntc_lcl_cd",
												"Z002"); // Z002   : 유지
										icCsCntcHisReg.setString("cntc_mcl_cd",
												"Z0022"); // Z0022  : 대출상태변경
										icCsCntcHisReg.setString(
												"cntc_smcl_cd", "Z00043"); // Z00043 : 연체발생
										icCsCntcHisReg.setString(
												"req_cs_rlt_cd", "01"); // 01     : 본인
										icCsCntcHisReg.setString(
												"cntc_infl_pth_dv_cd", "01"); // 01     : 사무계
										icCsCntcHisReg.setString("prc_orz_cd",
												BchCntcPrcOrzImConst.ORZ_CD
														.getCode());
										icCsCntcHisReg.setString("prc_om_no",
												BchCntcPrcOrzImConst.OM_CD
														.getCode());

										icCsCntcHisReg
												.setString(
														"cntc_dtl_txt",
														"연체등록 (연체발생일자 : "
																+ tAaRegTgtListInqDto
																		.getString("aa_occ_dt")
																+ ", 연체등록사유 : "
																+ KIntegrationCodeUtil
																		.getIgCdValiValNm(
																				"AA_REG_RSN_CD",
																				tAaRegTgtListInqDto
																						.getString("aa_reg_rsn_cd"))
																+ ")");
										LProtocolInitializeUtil
												.primitiveLMultiInitialize(icCsCntcHisReg);
										rcCsCntcHisReg = igCsCntcImIbc
												.regtCsCntcHis(icCsCntcHisReg); //##고객접촉내역등록
									}
								} else {

									// =============================================================================
									// ######### GeneralCodeBlock ##write  입력값 세팅 << 신규연체상태 INSERT ERROR >>
									// =============================================================================
									// [i사후연체기본연체등록]

									iAffcAaBasAaReg = "<< 신규연체상태 INSERT ERROR >>   "
											+ "대출번호 : "
											+ tAaRegTgtListInqDto
													.getString("ln_no")
											+ ", "
											+ "대출일련번호 : "
											+ tAaRegTgtListInqDto
													.getLong("ln_seq") + "\n\n";
									writer.writeBytes(iAffcAaBasAaReg);
									context.addErrorCount(this, 1); //##setErrorCnt

									// =============================================================================
									// ######### GeneralCodeBlock ##오류건수 + 1
									// =============================================================================
									tAaRegTgtListInqDto.setString("omss_yn",
											"Y");
									tAaRegPrcNcnMgtDto.setLong("err_ncn",
											tAaRegPrcNcnMgtDto
													.getLong("err_ncn") + 1);

									// =============================================================================
									// ######### GeneralCodeBlock ##continue
									// =============================================================================
									continue;
								}
							}
						}
					} catch (Exception e) {

						// =============================================================================
						// ######### GeneralCodeBlock ##write  입력값 세팅 << 연체기본 TABLE 처리 ERROR >>
						// =============================================================================
						// [i사후연체기본연체등록]

						String sAaDvNm = tAaRegTgtListInqDto
								.getString("aa_dv_cd") + ". "; //#s연체구분명

						if (KLDataConvertUtil.equals("0",
								tAaRegTgtListInqDto.getString("aa_dv_cd"))) {
							sAaDvNm = "(연체해제상태)";
						} else if (KLDataConvertUtil.equals("1",
								tAaRegTgtListInqDto.getString("aa_dv_cd"))) {
							sAaDvNm = "(연체상태)";
						} else if (KLDataConvertUtil.equals("2",
								tAaRegTgtListInqDto.getString("aa_dv_cd"))) {
							sAaDvNm = "(신규연체상태)";
						}

						iAffcAaBasAaReg = "<< 연체기본 TABLE 처리 ERROR " + sAaDvNm
								+ " >>   " + "대출번호 : "
								+ tAaRegTgtListInqDto.getString("ln_no") + ", "
								+ "대출일련번호 : "
								+ tAaRegTgtListInqDto.getLong("ln_seq") + ", "
								+ "연체구분코드/명 : " + sAaDvNm + "\n "
								+ "  --> 오류메세지 : "
								+ KExceptionUtil.getMessage(e);

						// DB 처리시 사용한 입력값 출력                        
						if (KLDataConvertUtil.equals("0",
								tAaRegTgtListInqDto.getString("aa_dv_cd"))) {
							iAffcAaBasAaReg = iAffcAaBasAaReg
									+ KInterfaceDataConvertUtil
											.convertDtoToJson(iAffcAaBasAaRelsSsUpd)
									+ "\n\n";
						} else if (KLDataConvertUtil.equals("1",
								tAaRegTgtListInqDto.getString("aa_dv_cd"))) {
							iAffcAaBasAaReg = iAffcAaBasAaReg
									+ KInterfaceDataConvertUtil
											.convertDtoToJson(iAffcAaBasAaSsUpd)
									+ "\n\n";
						} else if (KLDataConvertUtil.equals("2",
								tAaRegTgtListInqDto.getString("aa_dv_cd"))) {
							iAffcAaBasAaReg = iAffcAaBasAaReg
									+ KInterfaceDataConvertUtil
											.convertDtoToJson(iAffcAaBasNwAaReg)
									+ "\n\n";
						}
						writer.writeBytes(iAffcAaBasAaReg);
						context.addErrorCount(this, 1); //##setErrorCnt

						// =============================================================================
						// ######### GeneralCodeBlock ##오류건수 + 1
						// =============================================================================
						tAaRegTgtListInqDto.setString("omss_yn", "Y");
						tAaRegPrcNcnMgtDto.setLong("err_ncn",
								tAaRegPrcNcnMgtDto.getLong("err_ncn") + 1);

						// =============================================================================
						// ######### GeneralCodeBlock ##continue
						// =============================================================================
						continue;
					}

					//// 사후연체기본 (누적연체일수) 갱신
					{
						try {

							// =============================================================================
							// ######### GeneralCodeBlock ##사후연체기본[누적연체일수]수정  입력값 세팅
							// =============================================================================
							// [i사후연체기본누적연체일수수정]  

							iAffcAaBasAccuAaDcnUpd = KCommMngUtil.initLData(
									iAffcAaBasAccuAaDcnUpd, new String[] {
											"ln_no", "ln_seq", "bd_aa_ss_cd",
											"lon_pdt_lcl_cd", "lon_pdt_mcl_cd",
											"lon_pdt_smcl_cd", "aa_dcn",
											"accu_aa_dcn", "aa_yn",
											"aa_rels_dt", "aa_occ_dt",
											"aa_occ_bs_dt", "aa_reg_dt",
											"aa_reg_rsn_cd", "aa_rels_rsn_cd",
											"aa_tim", "aa_tim2", "exp_dt",
											"pa_fsr_dt", "cmpe_ls_dt",
											"nt_rpy_dt", "aa_crat_pttm_ln_amt",
											"aa_crat_pttm_ln_bal",
											"aa_reg_om_no", "jrsd_orz_cd",
											"del_yn", "aa_mgt_orz_dv_cd",
											"aa_mgt_orz_dist_dt",
											"aa_mgt_orz_cd", "aa_mgt_om_no",
											"aa_mgt_om_dist_dt", "csg_dt",
											"cscm_dept_nm", "csg_bd_chp_nm",
											"csg_bd_chp_tlno", "fs_prr_dt",
											"ch_prr_dt", "sms_snd_dt",
											"sms_snd_dv_cd", "rqe_orz_cd",
											"rqe_om_no", "msk_cm_ss_cd",
											"adn_cd", "adn_snd_dt",
											"adn_rr_yn", "ib_ob_dv_cd",
											"rem_prms_dt", "rem_prms_tm",
											"rge_prg_dt", "rge_prg_tm",
											"ls_csl_tp_cd", "ls_csl_om_no",
											"ls_csl_dt", "ls_csl_tm",
											"erpd_alm_bs_ym", "erd_aa_ge_scr",
											"erd_aa_ge_gd_cd",
											"erpd_alm_sgn_val",
											"erpd_alm_gd_cd", "erpd_alm_scr",
											"egd_rsn_cd", "fnn_cer_dctd_yn",
											"lsdb_cap_ln_acc_eyn", "fs_reg_dt",
											"prc_ip_adr", "prc_prog_id",
											"fs_in_usr_id", "fs_in_dtm",
											"ls_ch_usr_id", "ls_ch_dtm" },
									new String[] { "STRING", "LONG", "STRING",
											"STRING", "STRING", "STRING",
											"LONG", "LONG", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "LONG", "LONG",
											"STRING", "STRING", "STRING",
											"STRING", "BIGDECIMAL",
											"BIGDECIMAL", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"LONG", "STRING", "LONG", "STRING",
											"LONG", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING", "STRING",
											"STRING", "STRING" }, true);
							iAffcAaBasAccuAaDcnUpd.setString("ln_no",
									tAaRegTgtListInqDto.getString("ln_no"));
							iAffcAaBasAccuAaDcnUpd.setLong("ln_seq",
									tAaRegTgtListInqDto.getLong("ln_seq"));
							commonDao = new LCommonDao(
									"batch/lon/rl/job/aamgt/aaregjob/AaReg/uptAffcAaBasByAccuAaDcn",
									iAffcAaBasAccuAaDcnUpd); //##사후연체기본[누적연체일수]수정
							rAffcAaBasAccuAaDcnUpd = commonDao.executeUpdate();
						} catch (Exception e) {

							// =============================================================================
							// ######### GeneralCodeBlock ##write  입력값 세팅  << 누전역체일수 갱신 ERROR >>
							// =============================================================================
							// [i사후연체기본연체등록]  

							iAffcAaBasAaReg = "<< 누전역체일수 갱신 ERROR >>   "
									+ "대출번호 : "
									+ tAaRegTgtListInqDto.getString("ln_no")
									+ ", " + "대출일련번호 : "
									+ tAaRegTgtListInqDto.getLong("ln_seq")
									+ ", " + "오류메세지 : "
									+ KExceptionUtil.getMessage(e) + "\n";
							writer.writeBytes(iAffcAaBasAaReg);
							context.addErrorCount(this, 1); //##setErrorCnt

							// =============================================================================
							// ######### GeneralCodeBlock ##오류건수 + 1
							// =============================================================================
							tAaRegTgtListInqDto.setString("omss_yn", "Y");
							tAaRegPrcNcnMgtDto.setLong("err_ncn",
									tAaRegPrcNcnMgtDto.getLong("err_ncn") + 1);

							// =============================================================================
							// ######### GeneralCodeBlock ##continue
							// =============================================================================
							continue;
						}
					}

					// =============================================================================
					// ######### GeneralCodeBlock ##연체처리건수 + 1
					// =============================================================================
					tAaRegPrcNcnMgtDto.setLong("aa_prc_ncn",
							tAaRegPrcNcnMgtDto.getLong("aa_prc_ncn") + 1);
					context.addSuccessCount(this, 1); //##setSuccessCnt
					if (KLDataConvertUtil.equals(context.getTotalProgress(this)
							% context.getCommitCount(), 0)) {

						// =============================================================================
						// ######### GeneralCodeBlock ##commit
						// =============================================================================
						this.txCommit();
					}
				}
			}
		}

		// =============================================================================
		// ######### GeneralCodeBlock ##write  입력값 세팅 <<배치처리결과 건수 출력>>
		// =============================================================================
		// [i사후연체기본연체등록]

		iAffcAaBasAaReg = "\n========================================================================================연체등록 END=========>>>\n\n"
				+ "=========================================<<처리결과>>===========================================================\n"
				+ "-----------------------------------------------------------전체대상건수 = 생략건수 + 오류건수 + 연체처리건수\n"
				+ "전체대상건수     : "
				+ tAaRegPrcNcnMgtDto.getLong("all_ncn")
				+ "\n"
				+ "생략건수         : "
				+ tAaRegPrcNcnMgtDto.getLong("omss_ncn")
				+ "\n"
				+ "오류건수         : "
				+ tAaRegPrcNcnMgtDto.getLong("err_ncn")
				+ "\n"
				+ "연체대상건수     : "
				+ tAaRegPrcNcnMgtDto.getLong("aa_tgt_ncn")
				+ "\n"
				+ "연체처리건수     : "
				+ tAaRegPrcNcnMgtDto.getLong("aa_prc_ncn")
				+ "\n"
				+ "-----------------------------------------------------------SKIP(비영업일) 처리 대상건(단독CNT)\n"
				+ "비영업일처리건수 : "
				+ tAaRegPrcNcnMgtDto.getLong("nbz_day_prc_ncn")
				+ "\n"
				+ "-----------------------------------------------------------SKIP 처리 대상건(샹략건수에 포함)\n"
				+ "자동이체청구건수 : "
				+ tAaRegPrcNcnMgtDto.getLong("aut_trs_rq_ncn")
				+ "\n"
				+ "MBS매각대상건수  : "
				+ tAaRegPrcNcnMgtDto.getLong("mbs_sel_tgt_ncn")
				+ "\n"
				+ "-----------------------------------------------------------특수채권/미수이자 대상건(연체대상건수에 포함)\n"
				+ "특수채권건수     : "
				+ tAaRegPrcNcnMgtDto.getBigDecimal("spc_bd_ncn")
				+ "\n"
				+ "미수이자대상건수 : "
				+ tAaRegPrcNcnMgtDto.getLong("unc_irt_tgt_ncn")
				+ "\n"
				+ "-----------------------------------------------------------실제 INSERT/UPDATE 처리 건수 (연체처리건수)\n"
				+ "신규연체건수     : "
				+ tAaRegPrcNcnMgtDto.getLong("nw_aa_ncn")
				+ "\n"
				+ "현재연체건수     : "
				+ tAaRegPrcNcnMgtDto.getLong("now_aa_ncn")
				+ "\n"
				+ "현재연체해제건수 : "
				+ tAaRegPrcNcnMgtDto.getLong("now_aa_rels_ncn")
				+ "\n"
				+ "==================================================================================================================";
		writer.writeBytes(iAffcAaBasAaReg);
		try {

			// =============================================================================
			// ######### GeneralCodeBlock ##배치작업실행내역종료수정  입력값 세팅
			// =============================================================================
			// [i배치작업실행내역종료수정]  
			iBchOpExecHisEdUpd = new LData();
			iBchOpExecHisEdUpd.setString("bch_op_id", context.getJobId());
			iBchOpExecHisEdUpd.setString("jflw_id", context.getJobId() + "01");
			iBchOpExecHisEdUpd.setString("op_bs_dt", getStoreData("bsDt"));
			iBchOpExecHisEdUpd.setString("nml_err_dv_cd", "1");
			iBchOpExecHisEdUpd.setLong("bch_all_op_ncn",
					context.getTotalProgress(this));
			iBchOpExecHisEdUpd.setLong("bch_nml_prc_ncn",
					context.getTotalProgress(this));
			iBchOpExecHisEdUpd.setLong("bch_err_occ_ncn", 0);
			iBchOpExecHisEdUpd.setString("bch_op_err_mg_txt", "");
			KInterfaceInitContext.initContext(); //온라인 공통 모듈 호출전 Context 정보 설정
			LProtocolInitializeUtil
					.primitiveLMultiInitialize(iBchOpExecHisEdUpd);
			bchOpMgtCommCpbc.uptBchOpExecHisEd(iBchOpExecHisEdUpd); //##배치작업실행내역종료수정

			// =============================================================================
			// ######### GeneralCodeBlock ##commit
			// =============================================================================
			this.txCommit();
		} catch (Exception e) {
		}
		return SUCCEED;
	}
}
