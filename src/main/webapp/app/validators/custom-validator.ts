import { AbstractControl, FormControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class CustomValidator {

  public static NUMERIC_PATTERN = /^[\d]*$/;
  public static ALPHA_NUMERIC_PATTERN = /^[a-zA-Z0-9 ]*$/;
  public static PHONE_NUMBER_PATTERN = /^(?:\+88|88)?(01[3-9]\d{8})$/;
  public static SEARCH_TEXT_PATTERN = /^[a-zA-Z0-9@+\-_. ]*$/;
  public static NATURAL_TEXT_PATTERN = /^[a-zA-Z0-9@\-+_()#., ]*$/;
  public static URL_PATTERN = /^(https?|ftp)?(:\/\/[^\s\/$.?#].[^\s]*)?$/;

  static notOnlyWhitespace(control: FormControl): ValidationErrors | null {
    if (control.value != null && control.value.trim().length === 0) {
      return { notOnlyWhitespace: true };
    }
    return null;
  }

  static numberValidator(allowNullOrEmpty?: boolean): ValidatorFn{
    return (control: AbstractControl): ValidationErrors | null => {
      // Check for null or empty value based on allowNullOrEmpty parameter
      if (allowNullOrEmpty && (control.value === null || control.value === '')) {
        return null;
      }

      // Validate the value against the pattern
      if (!this.NUMERIC_PATTERN.test(control.value)){
        return { pattern: true};
      }
      return null
    }
  }

  static alphaNumericValidator(allowNullOrEmpty?: boolean): ValidatorFn{
    return (control: AbstractControl): ValidationErrors | null => {
      // Check for null or empty value based on allowNullOrEmpty parameter
      if (allowNullOrEmpty && (control.value === null || control.value === '')) {
        return null;
      }

      // Validate the value against the pattern
      if (!this.ALPHA_NUMERIC_PATTERN.test(control.value)){
        return { pattern: true};
      }
      return null
    }
  }

  static phoneNumberValidator(allowNullOrEmpty?: boolean): ValidatorFn{
    return (control: AbstractControl): ValidationErrors | null => {
      // Check for null or empty value based on allowNullOrEmpty parameter
      if (allowNullOrEmpty && (control.value === null || control.value === '')) {
        return null;
      }

      // Validate the value against the pattern
      if (!this.PHONE_NUMBER_PATTERN.test(control.value)){
        return { pattern: true};
      }
      return null
    }
  }

  static searchTextValidator(allowNullOrEmpty?: boolean): ValidatorFn{
    return (control: AbstractControl): ValidationErrors | null => {
      // Check for null or empty value based on allowNullOrEmpty parameter
      if (allowNullOrEmpty && (control.value === null || control.value === '')) {
        return null;
      }

      // Validate the value against the pattern
      if (!this.SEARCH_TEXT_PATTERN.test(control.value)){
        return { pattern: true};
      }
      return null
    }
  }
  static naturalTextValidator(allowNullOrEmpty?: boolean): ValidatorFn{
    return (control: AbstractControl): ValidationErrors | null => {
      // Check for null or empty value based on allowNullOrEmpty parameter
      if (allowNullOrEmpty && (control.value === null || control.value === '')) {
        return null;
      }

      // Validate the value against the pattern
      if (!this.NATURAL_TEXT_PATTERN.test(control.value)){
        return { pattern: true};
      }
      return null
    }
  }
  static urlValidator(allowNullOrEmpty?: boolean): ValidatorFn{
    return (control: AbstractControl): ValidationErrors | null => {
      // Check for null or empty value based on allowNullOrEmpty parameter
      if (allowNullOrEmpty && (control.value === null || control.value === '')) {
        return null;
      }

      // Validate the value against the pattern
      if (!this.URL_PATTERN.test(control.value)){
        return { pattern: true};
      }
      return null
    }
  }
}
