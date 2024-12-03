import { IInsuranceRegistration } from './insurance-registration.model';

export interface IInsuranceRegistrationAdmin {
  employeeId?: number;
  insuranceRegistrationDTOList?: IInsuranceRegistration[];
}

export class InsuranceRegistrationAdmin implements IInsuranceRegistrationAdmin {
  constructor(public employeeId?: number, public insuranceRegistrationDTOList?: IInsuranceRegistration[]) {}
}
