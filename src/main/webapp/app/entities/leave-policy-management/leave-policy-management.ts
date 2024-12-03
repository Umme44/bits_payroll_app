export class LeavePolicyManagement {
  policyTitle: string;
  description: string;
  leaveType: string;
  policyCode: string;
  gender: string;
  maritalStatus: string;
  hasLifeCycleLimit: boolean;
  lifeCycleLimitInDays: number;
  minDurationForSupportingDocumentInDays: number;
  requirementForSupportingDocument:string[] = [];
  minDurationForPriorLeaveApplyInDays: number;
  isDescriptionRequired: boolean;
  isAddressOnLeaveRequired: boolean;
  isPhoneNumberOnLeaveRequired: boolean;
  isConsecutiveLeaveInLifecycle: boolean;
  isBalanceCheckRequired: boolean;
  canAppliedByEmployee:boolean;
  allocationCriteria:boolean;
  hasAutoAllocationFromJoining: boolean;
  allocationType: string;
  willExpireInDays: number;
  autoDeductionPriority: number;
  leaveDurationStrategy: string;
  monthlyMaxLimit: number;
  isEncashableEachYear: boolean
  yearlyForwardingLimit : number;
  isForwardable : boolean;
  forwardingLimitOnLifetime: number;
  hasExpiry: boolean;
  isAutoDeductible: boolean;
  isHalfDayAllowed : boolean;
  hasMonthlyMaxLimit:boolean ;
  isPaidLeave:boolean;
  encashmentCriteria:boolean;
  hasFixedFactorAllocationOnLeaveDay: boolean;
  hasFixedFactorAllocationOnHoliday: boolean;
  allocationFactor: number;
  expirationCriteria: string;
  calenderYearAdvance: string;
  calenderMonthAdvance: string;
  perDayFixedFactor: string;
  perCalenderYearAllocation: number;
  calenderYearAdvancedB:boolean;
  calenderMonthAdvancedB:boolean;
  perDayFixedFactorB:boolean;
  allocationTypes: string[] = []



  constructor(
    policyTitle: string,
    description: string,
    leaveType: string,
    policyCode: string,
    gender: string,
    maritalStatus: string,
    requirementForSupportingDocument: string[] = [],
    minDurationForPriorLeaveApplyInDays: number,
    isDescriptionRequired: boolean,
    isAddressOnLeaveRequired: boolean,
    isPhoneNumberOnLeaveRequired: boolean,
    isConsecutiveLeaveInLifecycle: boolean,
    isBalanceCheckRequired: boolean,
    canAppliedByEmployee:boolean,
    allocationCriteria: boolean,
    hasExpiry: boolean,
    isAutoDeductible:boolean,
    isHalfDayAllowed:boolean,
    hasMonthlyMaxLimit:boolean,
    isPaidLeave:boolean,
    encashmentCriteria:boolean,
    hasAutoAllocationFromJoining: boolean,
    allocationType: string,
    willExpireInDays: number,
    autoDeductionPriority: number,
    leaveDurationStrategy: string,
    monthlyMaxLimit: number,
    isEncashableEachYear: boolean,
    yearlyForwardingLimit : number,
    isForwardable : boolean,
    forwardingLimitOnLifetime: number,
    hasFixedFactorAllocationOnLeaveDay: boolean,
    hasFixedFactorAllocationOnHoliday: boolean,
    allocationFactor: number,
    hasLifeCycleLimit: boolean,
    lifeCycleLimitInDays: number,
    expirationCriteria: string,
    calenderYearAdvance: string,
    calenderMonthAdvance: string,
    perDayFixedFactor: string,
    perCalenderYearAllocation: number,
    calenderYearAdvancedB:boolean,
    calenderMonthAdvancedB:boolean,
    perDayFixedFactorB:boolean,
    allocationTypes: string [],
    minDurationForSupportingDocumentInDays:number


  ) {
    this.policyTitle = policyTitle;
    this.description = description;
    this.leaveType = leaveType;
    this.policyCode = policyCode;
    this.gender = gender;
    this.maritalStatus = maritalStatus;
    this.isPhoneNumberOnLeaveRequired = isPhoneNumberOnLeaveRequired;
    this.requirementForSupportingDocument = requirementForSupportingDocument;
    this.minDurationForPriorLeaveApplyInDays = minDurationForPriorLeaveApplyInDays;
    this.isDescriptionRequired = isDescriptionRequired;
    this.isAddressOnLeaveRequired = isAddressOnLeaveRequired;
    this.isConsecutiveLeaveInLifecycle = isConsecutiveLeaveInLifecycle;
    this.isBalanceCheckRequired = isBalanceCheckRequired;
    this.canAppliedByEmployee = canAppliedByEmployee;
    this.allocationCriteria = allocationCriteria;
    this.hasExpiry = hasExpiry;
    this.isAutoDeductible = isAutoDeductible;
    this.isHalfDayAllowed = isHalfDayAllowed;
    this.hasMonthlyMaxLimit = hasMonthlyMaxLimit;
    this.isPaidLeave = isPaidLeave;
    this.encashmentCriteria = encashmentCriteria;
    this.hasAutoAllocationFromJoining = hasAutoAllocationFromJoining ;
    this.allocationType = allocationType;
    this.willExpireInDays = willExpireInDays ;
    this.autoDeductionPriority = autoDeductionPriority ;
    this.leaveDurationStrategy = leaveDurationStrategy ;
    this.monthlyMaxLimit = monthlyMaxLimit ;
    this.isEncashableEachYear = isEncashableEachYear ;
    this.yearlyForwardingLimit = yearlyForwardingLimit ;
    this.isForwardable = isForwardable ;
    this.forwardingLimitOnLifetime = forwardingLimitOnLifetime;
    this.hasFixedFactorAllocationOnLeaveDay = hasFixedFactorAllocationOnLeaveDay;
    this.hasFixedFactorAllocationOnHoliday = hasFixedFactorAllocationOnHoliday;
    this.allocationFactor = allocationFactor;
    this.hasLifeCycleLimit = hasLifeCycleLimit;
    this.lifeCycleLimitInDays = lifeCycleLimitInDays;
    this.expirationCriteria = expirationCriteria;
    this.calenderYearAdvance = calenderYearAdvance;
    this.calenderMonthAdvance = calenderMonthAdvance;
    this.perDayFixedFactor =  perDayFixedFactor;
    this.perCalenderYearAllocation = perCalenderYearAllocation;
    this.calenderYearAdvancedB = calenderYearAdvancedB;
    this.calenderMonthAdvancedB = calenderMonthAdvancedB;
    this.perDayFixedFactorB = perDayFixedFactorB;
    this.allocationTypes = allocationTypes;
    this.minDurationForSupportingDocumentInDays = minDurationForSupportingDocumentInDays;
  }



}
