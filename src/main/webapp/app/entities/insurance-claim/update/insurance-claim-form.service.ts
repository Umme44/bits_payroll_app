import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInsuranceClaim, NewInsuranceClaim } from '../insurance-claim.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInsuranceClaim for edit and NewInsuranceClaimFormGroupInput for create.
 */
type InsuranceClaimFormGroupInput = IInsuranceClaim | PartialWithRequiredKeyOf<NewInsuranceClaim>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInsuranceClaim | NewInsuranceClaim> = Omit<T, 'createdAt' | 'updatedAt' | 'settlementDate' | 'paymentDate' | 'regretDate'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  settlementDate?: string | null;
  paymentDate?: string | null;
  regretDate?: string | null;
};

type InsuranceClaimFormRawValue = FormValueOf<IInsuranceClaim>;

type NewInsuranceClaimFormRawValue = FormValueOf<NewInsuranceClaim>;

type InsuranceClaimFormDefaults = Pick<NewInsuranceClaim, 'id' | 'createdAt' | 'updatedAt' | 'settlementDate' | 'paymentDate' | 'regretDate'>;

type InsuranceClaimFormGroupContent = {
  id: FormControl<InsuranceClaimFormRawValue['id'] | NewInsuranceClaim['id']>;
  insuranceCardId: FormControl<InsuranceClaimFormRawValue['insuranceCardId']>;
  policyHolderPin: FormControl<InsuranceClaimFormRawValue['policyHolderPin']>;
  policyHolderName: FormControl<InsuranceClaimFormRawValue['policyHolderName']>;
  registrationName: FormControl<InsuranceClaimFormRawValue['registrationName']>;
  relation: FormControl<InsuranceClaimFormRawValue['relation']>;
  settlementDate: FormControl<InsuranceClaimFormRawValue['settlementDate']>;
  paymentDate: FormControl<InsuranceClaimFormRawValue['paymentDate']>;
  regretDate: FormControl<InsuranceClaimFormRawValue['regretDate']>;
  regretReason: FormControl<InsuranceClaimFormRawValue['regretReason']>;
  claimedAmount: FormControl<InsuranceClaimFormRawValue['claimedAmount']>;
  settledAmount: FormControl<InsuranceClaimFormRawValue['settledAmount']>;
  claimStatus: FormControl<InsuranceClaimFormRawValue['claimStatus']>;
  createdAt: FormControl<InsuranceClaimFormRawValue['createdAt']>;
  updatedAt: FormControl<InsuranceClaimFormRawValue['updatedAt']>;
  createdById: FormControl<InsuranceClaimFormRawValue['createdById']>;
  updatedById: FormControl<InsuranceClaimFormRawValue['updatedById']>;
  insuranceRegistrationId: FormControl<InsuranceClaimFormRawValue['insuranceRegistrationId']>;
};

export type InsuranceClaimFormGroup = FormGroup<InsuranceClaimFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InsuranceClaimFormService {
  createInsuranceClaimFormGroup(insuranceClaim: InsuranceClaimFormGroupInput = { id: null }): InsuranceClaimFormGroup {
    const insuranceClaimRawValue = this.convertInsuranceClaimToInsuranceClaimRawValue({
      ...this.getFormDefaults(),
      ...insuranceClaim,
    });
    return new FormGroup<InsuranceClaimFormGroupContent>({
      id: new FormControl(
        { value: insuranceClaimRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      insuranceCardId: new FormControl(insuranceClaimRawValue.insuranceCardId, {
        validators: [Validators.required],
      }),
      policyHolderPin: new FormControl(insuranceClaimRawValue.policyHolderPin),
      policyHolderName: new FormControl(insuranceClaimRawValue.policyHolderName),
      registrationName: new FormControl(insuranceClaimRawValue.registrationName),
      relation: new FormControl(insuranceClaimRawValue.relation),
      settlementDate: new FormControl(insuranceClaimRawValue.settlementDate),
      paymentDate: new FormControl(insuranceClaimRawValue.paymentDate),
      regretDate: new FormControl(insuranceClaimRawValue.regretDate),
      regretReason: new FormControl(insuranceClaimRawValue.regretReason),
      claimedAmount: new FormControl(insuranceClaimRawValue.claimedAmount, {
        validators: [Validators.required],
      }),
      settledAmount: new FormControl(insuranceClaimRawValue.settledAmount),
      claimStatus: new FormControl(insuranceClaimRawValue.claimStatus, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(insuranceClaimRawValue.createdAt),
      updatedAt: new FormControl(insuranceClaimRawValue.updatedAt),
      createdById: new FormControl(insuranceClaimRawValue.createdById),
      updatedById: new FormControl(insuranceClaimRawValue.updatedById),
      insuranceRegistrationId: new FormControl(insuranceClaimRawValue.insuranceRegistrationId, {
        validators: [Validators.required],
      }),
    });
  }

  getInsuranceClaim(form: InsuranceClaimFormGroup): IInsuranceClaim | NewInsuranceClaim {
    return this.convertInsuranceClaimRawValueToInsuranceClaim(
      form.getRawValue() as InsuranceClaimFormRawValue | NewInsuranceClaimFormRawValue
    );
  }

  resetForm(form: InsuranceClaimFormGroup, insuranceClaim: InsuranceClaimFormGroupInput): void {
    const insuranceClaimRawValue = this.convertInsuranceClaimToInsuranceClaimRawValue({ ...this.getFormDefaults(), ...insuranceClaim });
    form.reset(
      {
        ...insuranceClaimRawValue,
        id: { value: insuranceClaimRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InsuranceClaimFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      settlementDate: null,
      paymentDate: null,
      regretDate: null,
    };
  }

  private convertInsuranceClaimRawValueToInsuranceClaim(
    rawInsuranceClaim: InsuranceClaimFormRawValue | NewInsuranceClaimFormRawValue
  ): IInsuranceClaim | NewInsuranceClaim {
    return {
      ...rawInsuranceClaim,
      createdAt: dayjs(rawInsuranceClaim.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawInsuranceClaim.updatedAt, DATE_TIME_FORMAT),
      settlementDate: dayjs(rawInsuranceClaim.settlementDate, DATE_TIME_FORMAT),
      paymentDate: dayjs(rawInsuranceClaim.paymentDate, DATE_TIME_FORMAT),
      regretDate: dayjs(rawInsuranceClaim.regretDate, DATE_TIME_FORMAT),
    };
  }

  private convertInsuranceClaimToInsuranceClaimRawValue(
    insuranceClaim: IInsuranceClaim | (Partial<NewInsuranceClaim> & InsuranceClaimFormDefaults)
  ): InsuranceClaimFormRawValue | PartialWithRequiredKeyOf<NewInsuranceClaimFormRawValue> {
    return {
      ...insuranceClaim,
      createdAt: insuranceClaim.createdAt ? insuranceClaim.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: insuranceClaim.updatedAt ? insuranceClaim.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      settlementDate: insuranceClaim.settlementDate ? insuranceClaim.settlementDate.format(DATE_TIME_FORMAT) : null,
      paymentDate: insuranceClaim.paymentDate ? insuranceClaim.paymentDate.format(DATE_TIME_FORMAT) : null,
      regretDate: insuranceClaim.regretDate ? insuranceClaim.regretDate.format(DATE_TIME_FORMAT) : null,
    };
  }
}
