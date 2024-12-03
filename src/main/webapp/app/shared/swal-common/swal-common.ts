import {
  SWAL_APPLIED_ICON,
  SWAL_APPLIED_TEXT,
  SWAL_APPLIED_TIMER,
  SWAL_APPROVE_CONFIRMATION,
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED,
  SWAL_APPROVED_ICON,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DEFAULT_TIMER,
  SWAL_DELETE_CONFIRMATION,
  SWAL_DELETED_ICON,
  SWAL_DELETED_TEXT,
  SWAL_DELETED_TIMER,
  SWAL_DENIED_BTN_TIMER,
  SWAL_DENY_BTN_TEXT,
  SWAL_DENY_BUTTON_SELECT,
  SWAL_DENY_BUTTON_SELECT_ICON,
  SWAL_INFO_ICON,
  SWAL_REJECT_CONFIRMATION,
  SWAL_REJECTED,
  SWAL_REJECTED_ICON,
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
  SWAL_RESPONSE_ERROR_TIMER,
  SWAL_SAVED_ICON,
  SWAL_SAVED_TEXT,
  SWAL_SAVED_TIMER,
  SWAL_SUCCESS_ICON,
  SWAL_UPDATED_ICON,
  SWAL_UPDATED_TEXT,
  SWAL_UPDATED_TIMER,
  SWAL_WARNING_ICON,
} from './swal.properties.constant';
import Swal from 'sweetalert2';
import { DANGER_COLOR, INFO_COLOR, PRIMARY_COLOR } from 'app/config/color.code.constant';

export const swalChangesNotSaved = (): void => {
  Swal.fire({
    icon: SWAL_DENY_BUTTON_SELECT_ICON,
    text: SWAL_DENY_BUTTON_SELECT,
    timer: SWAL_DENIED_BTN_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnRequestError = (): void => {
  Swal.fire({
    icon: SWAL_RESPONSE_ERROR_ICON,
    text: SWAL_RESPONSE_ERROR_TEXT,
    timer: SWAL_RESPONSE_ERROR_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnRequestErrorWithBackEndErrorTitle = (message: string, timer = SWAL_RESPONSE_ERROR_TIMER): void => {
  Swal.fire({
    icon: SWAL_RESPONSE_ERROR_ICON,
    text: message ?? SWAL_RESPONSE_ERROR_TEXT,
    timer,
    showConfirmButton: false,
  });
};

export const swalOnAvailableVehicleNotFound = (): void => {
  Swal.fire({
    icon: SWAL_RESPONSE_ERROR_ICON,
    text: 'We can not proceed with your request due to unavailability of cars. Please find an UBER.',
    timer: 3000,
    showConfirmButton: false,
  });
};

export const swalOnDeleteConfirmation = (): Promise<any> => {
  return Swal.fire({
    text: SWAL_DELETE_CONFIRMATION,
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalOnDeleteSuccess = (): void => {
  Swal.fire({
    icon: SWAL_DELETED_ICON,
    text: SWAL_DELETED_TEXT,
    timer: SWAL_DELETED_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnSavedSuccess = (): void => {
  Swal.fire({
    icon: SWAL_SAVED_ICON,
    text: SWAL_SAVED_TEXT,
    timer: SWAL_SAVED_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnSentForApproval = (): void => {
  Swal.fire({
    icon: SWAL_SAVED_ICON,
    text: 'Sent For Approval!',
    timer: SWAL_SAVED_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnApprovalConfirmation = (): Promise<any> => {
  return Swal.fire({
    text: SWAL_APPROVE_CONFIRMATION,
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalOnChangeSalaryVisibilityConfirmation = (message: string): Promise<any> => {
  return Swal.fire({
    text: message,
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalOnBatchDeleteConfirmation = (warningMessage: string): Promise<any> => {
  return Swal.fire({
    text: warningMessage,
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalOnGeneralConfirmation = (confirmationText: string): Promise<any> => {
  return Swal.fire({
    text: confirmationText,
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalOnApprovedSuccess = (): void => {
  Swal.fire({
    icon: SWAL_APPROVED_ICON,
    text: SWAL_APPROVED,
    timer: SWAL_APPROVE_REJECT_TIMER,
    showConfirmButton: false,
  });
};

export const swalShowInfoMessage = (infoText: string, swalTimer = 3000, showConfirmButton = false): void => {
  Swal.fire({
    icon: SWAL_INFO_ICON,
    text: infoText,
    timer: swalTimer,
    showConfirmButton,
  });
};

export const swalFailedToApprove = (): void => {
  Swal.fire({
    icon: SWAL_DELETED_ICON,
    text: 'Failed to Approve!',
    timer: SWAL_APPROVE_REJECT_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnRejectedSuccess = (): void => {
  Swal.fire({
    icon: SWAL_REJECTED_ICON,
    text: SWAL_REJECTED,
    timer: SWAL_APPROVE_REJECT_TIMER,
    showConfirmButton: false,
  });
};

export const swalFailedToReject = (): void => {
  Swal.fire({
    icon: SWAL_DELETED_ICON,
    text: 'Failed to Reject!',
    timer: SWAL_APPROVE_REJECT_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnRejectConfirmation = (): Promise<any> => {
  return Swal.fire({
    text: SWAL_REJECT_CONFIRMATION,
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalOnUpdatedSuccess = (): void => {
  Swal.fire({
    icon: SWAL_UPDATED_ICON,
    text: SWAL_UPDATED_TEXT,
    timer: SWAL_UPDATED_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnAppliedSuccess = (): void => {
  Swal.fire({
    icon: SWAL_APPLIED_ICON,
    text: SWAL_APPLIED_TEXT,
    timer: SWAL_APPLIED_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnDisburseConfirmation = (): Promise<any> => {
  return Swal.fire({
    text: 'Disburse ?',
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalOnDisburseSuccess = (): void => {
  Swal.fire({
    icon: SWAL_SUCCESS_ICON,
    text: 'Disbursed',
    timer: SWAL_DEFAULT_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnSelection = (btn1Text: any, btn2Text: any, allowOutSideClick = true): Promise<any> => {
  return Swal.fire({
    text: 'Type ?',
    allowOutsideClick: allowOutSideClick,
    showDenyButton: true,
    confirmButtonText: btn1Text,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: btn2Text,
    denyButtonColor: INFO_COLOR,
  });
};

export const swalOnGenerateSuccess = (text1: string): void => {
  Swal.fire({
    icon: SWAL_SAVED_ICON,
    text: text1,
    timer: SWAL_SAVED_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnHoldOrUnholdConfirmation = (message: string): Promise<any> => {
  return Swal.fire({
    text: message,
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalOnHoldOrUnholdSuccess = (): void => {
  Swal.fire({
    icon: SWAL_UPDATED_ICON,
    text: SWAL_UPDATED_TEXT,
    timer: SWAL_UPDATED_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnRejectionForNomineeReport = (): void => {
  Swal.fire({
    text: 'Total 100% is required!',
    icon: SWAL_REJECTED_ICON,
    timer: 3000,
    showConfirmButton: false,
  });
};

export const swalClose = (): void => {
  Swal.close();
};

export const swalOnLoading = (loadText: string): void => {
  Swal.fire({
    html: loadText + '...',
    allowOutsideClick: false,
    timerProgressBar: true,
    didOpen(): void {
      Swal.showLoading();
    },
  });
};

export const swalOnChildAgeOver25 = (): void => {
  Swal.fire({
    icon: SWAL_REJECTED_ICON,
    text: 'Children Over 25 Years Can Not Be Registered',
    timer: 3000,
    showConfirmButton: false,
  });
};

export const swalOnTotalExpensesLessThanOne = (): void => {
  Swal.fire({
    icon: SWAL_REJECTED_ICON,
    text: 'Total Claim Amount Must Be Greater Than 0',
    timer: 3000,
    showConfirmButton: false,
  });
};

export const swalOnSeflOrSpouseExist = (): void => {
  Swal.fire({
    icon: SWAL_REJECTED_ICON,
    text: 'You Are Not Allowed To Register Yourself or Your Spouse More Than One Time!',
    timer: 4000,
    showConfirmButton: false,
  });
};

export const swalOnLoadingCustomLoader = (loadText = '', showTicToe = false): void => {
  let ticToeVar = '/';
  Swal.fire({
    html: loadText + '<b> </b>...',
    allowOutsideClick: false,
    timerProgressBar: true,
    didOpen(): void {
      Swal.showLoading();
      const b = Swal.getHtmlContainer()!.querySelector('b');
      setInterval(() => {
        if (showTicToe === true) {
          if (ticToeVar === '/') {
            ticToeVar = '\\';
          } else {
            ticToeVar = '/';
          }
          b!.textContent = ticToeVar;
        }
      }, 50);
    },
  });
};

export const swalSuccessWithMessage = (message: string): void => {
  Swal.fire({
    icon: SWAL_SUCCESS_ICON,
    text: message ?? 'Success',
    timer: SWAL_DEFAULT_TIMER,
    showConfirmButton: false,
  });
};

export const swalForWarningWithMessage = (message: string, timer = SWAL_DEFAULT_TIMER): void => {
  Swal.fire({
    icon: SWAL_WARNING_ICON,
    text: message,
    timer,
    showConfirmButton: false,
  });
};

export const swalForErrorWithMessage = (message: string, timer = SWAL_RESPONSE_ERROR_TIMER): void => {
  Swal.fire({
    icon: SWAL_RESPONSE_ERROR_ICON,
    text: message,
    timer,
    showConfirmButton: false,
  });
};

export const swalIfNotLineManager = (): void => {
  Swal.fire({
    icon: SWAL_REJECTED_ICON,
    text: 'Opps! Seems like you are not allowed to access this page.',
    timer: 2500,
    showConfirmButton: false,
  });
};

export const swalSelectValidImage = (): void => {
  Swal.fire({
    icon: SWAL_RESPONSE_ERROR_ICON,
    text: 'Only JPEG image type is allowed!',
    timer: SWAL_RESPONSE_ERROR_TIMER,
    showConfirmButton: false,
  });
};

export const swalOnNoUnregisteredRelation = (): void => {
  Swal.fire({
    icon: SWAL_RESPONSE_ERROR_ICON,
    text: 'You have registered all of the available relation types!',
    timer: SWAL_RESPONSE_ERROR_TIMER,
    showConfirmButton: false,
  });
};

export const swalCopyAlert = (): void => {
  Swal.fire({
    icon: 'success',
    text: 'Copied',
    showConfirmButton: false,
    timer: 1000,
  });
};

// export const swalOnChangeVisibilityStatus = (confirmBtnText: string, denyBtnText: string): Promise<any> => {
//   Swal.fire({
//     icon: SWAL_WARNING_ICON,
//     text: 'Choose your action.',
//     showDenyButton: true,
//     confirmButtonText: confirmBtnText,
//     denyButtonText: denyBtnText,
//   });
// };


export const swalPatternError = (): void => {
  Swal.fire({
    icon: 'error',
    text: 'Invalid character detected!',
    showConfirmButton: false,
    timer: 2000,
  });
};

