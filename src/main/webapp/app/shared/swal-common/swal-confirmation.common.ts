import Swal from 'sweetalert2';
import { SWAL_CANCEL_BTN_TEXT, SWAL_CONFIRM, SWAL_CONFIRM_BTN_TEXT, SWAL_DENY_BTN_TEXT } from './swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../config/color.code.constant';

export const swalConfirmationCommon = (): Promise<any> => {
  return Swal.fire({
    text: SWAL_CONFIRM,
    showDenyButton: true,
    confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

export const swalConfirmationWithMessage = (message: string, confirmBtnText = SWAL_CONFIRM_BTN_TEXT): Promise<any> => {
  return Swal.fire({
    text: message,
    showDenyButton: true,
    confirmButtonText: confirmBtnText,
    confirmButtonColor: PRIMARY_COLOR,
    denyButtonText: SWAL_DENY_BTN_TEXT,
    denyButtonColor: DANGER_COLOR,
  });
};

/**
 *
 * @param message
 * @param confirmBtnText
 * @param cancelBtnText
 * @param denyBtnText
 */
// example -> swalConfirmationWithMessageAndThreeButton(params)
// .then((result) => {
//   if (result.isConfirmed) {
//     Swal.fire('Saved!', '', 'success')
//   } else if (result.isDenied) {
//     Swal.fire('Changes are not saved', '', 'info')
//   }
//   } else if (result.isDismissed) {
//     Swal.fire('you clicked on cancel button', '', 'info')
//   }
// })
// just uncomment and use on your desired place.

export const swalConfirmationWithMessageAndThreeButton = (
  message: string,
  textMsg = '',
  confirmBtnText = SWAL_CONFIRM_BTN_TEXT,
  denyBtnText = SWAL_DENY_BTN_TEXT,
  cancelBtnText = SWAL_CANCEL_BTN_TEXT
): Promise<any> => {
  return Swal.fire({
    title: message,
    text: textMsg,
    showDenyButton: true,
    showCancelButton: true,
    showConfirmButton: true,
    confirmButtonText: confirmBtnText,
    denyButtonText: denyBtnText,
    cancelButtonText: cancelBtnText,
  });
};
