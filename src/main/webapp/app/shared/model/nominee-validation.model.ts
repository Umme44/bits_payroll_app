export interface INomineeValidation {
  isNidVerificationRequired?: boolean;
  isNidVerified?: boolean;
  isGuardianNidVerificationRequired?: boolean;
  isGuardianNidVerified?: boolean;
  remainingSharePercentage?: number;
  doesSharePercentageExceed?: boolean;
}
