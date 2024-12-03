import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import {ITaxAcknowledgementReceipt, NewTaxAcknowledgementReceipt} from "../tax-acknowledgement-receipt.model";
import {AcknowledgementStatus} from "../../../entities/enumerations/acknowledgement-status.model";
import {CustomValidator} from "../../../validators/custom-validator";
/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITaxAcknowledgementReceipt for edit and NewTaxAcknowledgementReceiptFormGroupInput for create.
 */
type TaxAcknowledgementReceiptFormGroupInput = ITaxAcknowledgementReceipt | PartialWithRequiredKeyOf<NewTaxAcknowledgementReceipt>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITaxAcknowledgementReceipt | NewTaxAcknowledgementReceipt> = Omit<
  T,
  'receivedAt' | 'createdAt' | 'updatedAt'
> & {
  receivedAt?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

type TaxAcknowledgementReceiptFormRawValue = FormValueOf<ITaxAcknowledgementReceipt>;

type NewTaxAcknowledgementReceiptFormRawValue = FormValueOf<NewTaxAcknowledgementReceipt>;

type TaxAcknowledgementReceiptFormDefaults = Pick<NewTaxAcknowledgementReceipt, 'id' | 'receivedAt' | 'createdAt' | 'updatedAt' | 'acknowledgementStatus'>;

type TaxAcknowledgementReceiptFormGroupContent = {
  id: FormControl<TaxAcknowledgementReceiptFormRawValue['id'] | NewTaxAcknowledgementReceipt['id']>;
  pin: FormControl<TaxAcknowledgementReceiptFormRawValue['pin']>;
  name: FormControl<TaxAcknowledgementReceiptFormRawValue['name']>;
  designation: FormControl<TaxAcknowledgementReceiptFormRawValue['designation']>;
  tinNumber: FormControl<TaxAcknowledgementReceiptFormRawValue['tinNumber']>;
  receiptNumber: FormControl<TaxAcknowledgementReceiptFormRawValue['receiptNumber']>;
  taxesCircle: FormControl<TaxAcknowledgementReceiptFormRawValue['taxesCircle']>;
  taxesZone: FormControl<TaxAcknowledgementReceiptFormRawValue['taxesZone']>;
  assessmentYear: FormControl<TaxAcknowledgementReceiptFormRawValue['assessmentYear']>;
  dateOfSubmission: FormControl<TaxAcknowledgementReceiptFormRawValue['dateOfSubmission']>;
  filePath: FormControl<TaxAcknowledgementReceiptFormRawValue['filePath']>;
  acknowledgementStatus: FormControl<TaxAcknowledgementReceiptFormRawValue['acknowledgementStatus']>;
  receivedAt: FormControl<TaxAcknowledgementReceiptFormRawValue['receivedAt']>;
  createdAt: FormControl<TaxAcknowledgementReceiptFormRawValue['createdAt']>;
  updatedAt: FormControl<TaxAcknowledgementReceiptFormRawValue['updatedAt']>;
  fiscalYearId: FormControl<TaxAcknowledgementReceiptFormRawValue['fiscalYearId']>;
  employeeId: FormControl<TaxAcknowledgementReceiptFormRawValue['employeeId']>;
  receivedById: FormControl<TaxAcknowledgementReceiptFormRawValue['receivedById']>;
  createdById: FormControl<TaxAcknowledgementReceiptFormRawValue['createdById']>;
  updatedById: FormControl<TaxAcknowledgementReceiptFormRawValue['updatedById']>;
};

export type TaxAcknowledgementReceiptFormGroup = FormGroup<TaxAcknowledgementReceiptFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserTaxAcknowledgementReceiptFormService {
  createTaxAcknowledgementReceiptFormGroup(
    taxAcknowledgementReceipt: TaxAcknowledgementReceiptFormGroupInput = { id: null }
  ): TaxAcknowledgementReceiptFormGroup {
    const taxAcknowledgementReceiptRawValue = this.convertTaxAcknowledgementReceiptToTaxAcknowledgementReceiptRawValue({
      ...this.getFormDefaults(),
      ...taxAcknowledgementReceipt,
    });
    return new FormGroup<TaxAcknowledgementReceiptFormGroupContent>({
      id: new FormControl(
        { value: taxAcknowledgementReceiptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      pin: new FormControl(taxAcknowledgementReceiptRawValue.pin),
      name: new FormControl(taxAcknowledgementReceiptRawValue.name),
      designation: new FormControl(taxAcknowledgementReceiptRawValue.designation),
      tinNumber: new FormControl(taxAcknowledgementReceiptRawValue.tinNumber, {
        validators: [Validators.required, Validators.minLength(12), Validators.maxLength(250), CustomValidator.numberValidator()],
      }),
      receiptNumber: new FormControl(taxAcknowledgementReceiptRawValue.receiptNumber, {
        validators: [Validators.required, Validators.maxLength(250), CustomValidator.numberValidator()],
      }),
      taxesCircle: new FormControl(taxAcknowledgementReceiptRawValue.taxesCircle, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      taxesZone: new FormControl(taxAcknowledgementReceiptRawValue.taxesZone, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      assessmentYear: new FormControl(taxAcknowledgementReceiptRawValue.assessmentYear),
      dateOfSubmission: new FormControl(taxAcknowledgementReceiptRawValue.dateOfSubmission, {
        validators: [Validators.required],
      }),
      filePath: new FormControl(taxAcknowledgementReceiptRawValue.filePath),
      acknowledgementStatus: new FormControl(taxAcknowledgementReceiptRawValue.acknowledgementStatus),
      receivedAt: new FormControl(taxAcknowledgementReceiptRawValue.receivedAt),
      createdAt: new FormControl(taxAcknowledgementReceiptRawValue.createdAt),
      updatedAt: new FormControl(taxAcknowledgementReceiptRawValue.updatedAt),
      fiscalYearId: new FormControl(taxAcknowledgementReceiptRawValue.fiscalYearId, {
        validators: [Validators.required],
      }),
      employeeId: new FormControl(taxAcknowledgementReceiptRawValue.employeeId),
      receivedById: new FormControl(taxAcknowledgementReceiptRawValue.receivedById),
      createdById: new FormControl(taxAcknowledgementReceiptRawValue.createdById),
      updatedById: new FormControl(taxAcknowledgementReceiptRawValue.updatedById),
    });
  }

  getTaxAcknowledgementReceipt(form: TaxAcknowledgementReceiptFormGroup): ITaxAcknowledgementReceipt | NewTaxAcknowledgementReceipt {
    return this.convertTaxAcknowledgementReceiptRawValueToTaxAcknowledgementReceipt(
      form.getRawValue() as TaxAcknowledgementReceiptFormRawValue | NewTaxAcknowledgementReceiptFormRawValue
    );
  }

  resetForm(form: TaxAcknowledgementReceiptFormGroup, taxAcknowledgementReceipt: TaxAcknowledgementReceiptFormGroupInput): void {
    const taxAcknowledgementReceiptRawValue = this.convertTaxAcknowledgementReceiptToTaxAcknowledgementReceiptRawValue({
      ...this.getFormDefaults(),
      ...taxAcknowledgementReceipt,
    });
    form.reset(
      {
        ...taxAcknowledgementReceiptRawValue,
        id: { value: taxAcknowledgementReceiptRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TaxAcknowledgementReceiptFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      receivedAt: currentTime,
      createdAt: currentTime,
      updatedAt: currentTime,
      acknowledgementStatus: AcknowledgementStatus.SUBMITTED
    };
  }

  private convertTaxAcknowledgementReceiptRawValueToTaxAcknowledgementReceipt(
    rawTaxAcknowledgementReceipt: TaxAcknowledgementReceiptFormRawValue | NewTaxAcknowledgementReceiptFormRawValue
  ): ITaxAcknowledgementReceipt | NewTaxAcknowledgementReceipt {
    return {
      ...rawTaxAcknowledgementReceipt,
      receivedAt: dayjs(rawTaxAcknowledgementReceipt.receivedAt, DATE_TIME_FORMAT),
      createdAt: dayjs(rawTaxAcknowledgementReceipt.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawTaxAcknowledgementReceipt.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertTaxAcknowledgementReceiptToTaxAcknowledgementReceiptRawValue(
    taxAcknowledgementReceipt: ITaxAcknowledgementReceipt | (Partial<NewTaxAcknowledgementReceipt> & TaxAcknowledgementReceiptFormDefaults)
  ): TaxAcknowledgementReceiptFormRawValue | PartialWithRequiredKeyOf<NewTaxAcknowledgementReceiptFormRawValue> {
    return {
      ...taxAcknowledgementReceipt,
      receivedAt: taxAcknowledgementReceipt.receivedAt ? taxAcknowledgementReceipt.receivedAt.format(DATE_TIME_FORMAT) : undefined,
      createdAt: taxAcknowledgementReceipt.createdAt ? taxAcknowledgementReceipt.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: taxAcknowledgementReceipt.updatedAt ? taxAcknowledgementReceipt.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
