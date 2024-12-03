export class CommonUtil {
  public static isNullOrEmpty = (value:any): boolean => {
    return value === null || value === undefined || value === '';
  };
}
