import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrganization, NewOrganization } from '../organization.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganization for edit and NewOrganizationFormGroupInput for create.
 */
type OrganizationFormGroupInput = IOrganization | PartialWithRequiredKeyOf<NewOrganization>;

type OrganizationFormDefaults = Pick<NewOrganization, 'id' | 'hasOrganizationStamp'>;

type OrganizationFormGroupContent = {
  id: FormControl<IOrganization['id'] | NewOrganization['id']>;
  shortName: FormControl<IOrganization['shortName']>;
  fullName: FormControl<IOrganization['fullName']>;
  slogan: FormControl<IOrganization['slogan']>;
  domainName: FormControl<IOrganization['domainName']>;
  emailAddress: FormControl<IOrganization['emailAddress']>;
  hrEmailAddress: FormControl<IOrganization['hrEmailAddress']>;
  noReplyEmailAddress: FormControl<IOrganization['noReplyEmailAddress']>;
  contactNumber: FormControl<IOrganization['contactNumber']>;
  financeManagerPIN: FormControl<IOrganization['financeManagerPIN']>;
  financeManagerSignature: FormControl<IOrganization['financeManagerSignature']>;
  logo: FormControl<IOrganization['logo']>;
  documentLetterHead: FormControl<IOrganization['documentLetterHead']>;
  pfStatementLetterHead: FormControl<IOrganization['pfStatementLetterHead']>;
  taxStatementLetterHead: FormControl<IOrganization['taxStatementLetterHead']>;
  nomineeLetterHead: FormControl<IOrganization['nomineeLetterHead']>;
  salaryPayslipLetterHead: FormControl<IOrganization['salaryPayslipLetterHead']>;
  festivalBonusPayslipLetterHead: FormControl<IOrganization['festivalBonusPayslipLetterHead']>;
  recruitmentRequisitionLetterHead: FormControl<IOrganization['recruitmentRequisitionLetterHead']>;
  hasOrganizationStamp: FormControl<IOrganization['hasOrganizationStamp']>;
  organizationStamp: FormControl<IOrganization['organizationStamp']>;
  linkedin: FormControl<IOrganization['linkedin']>;
  twitter: FormControl<IOrganization['twitter']>;
  facebook: FormControl<IOrganization['facebook']>;
  youtube: FormControl<IOrganization['youtube']>;
  instagram: FormControl<IOrganization['instagram']>;
  whatsapp: FormControl<IOrganization['whatsapp']>;
};

export type OrganizationFormGroup = FormGroup<OrganizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationFormService {
  createOrganizationFormGroup(organization: OrganizationFormGroupInput = { id: null }): OrganizationFormGroup {
    const organizationRawValue = {
      ...this.getFormDefaults(),
      ...organization,
    };
    return new FormGroup<OrganizationFormGroupContent>({
      id: new FormControl(
        { value: organizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      shortName: new FormControl(organizationRawValue.shortName, {
        validators: [Validators.required, Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
      fullName: new FormControl(organizationRawValue.fullName, {
        validators: [Validators.required, Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
      slogan: new FormControl(organizationRawValue.slogan, {
        validators: [Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
      domainName: new FormControl(organizationRawValue.domainName, {
        validators: [Validators.required, Validators.maxLength(255), CustomValidator.urlValidator()],
      }),
      emailAddress: new FormControl(organizationRawValue.emailAddress, {
        validators: [Validators.required, Validators.maxLength(255), Validators.email],
      }),
      hrEmailAddress: new FormControl(organizationRawValue.hrEmailAddress, {
        validators: [Validators.maxLength(255), Validators.email],
      }),
      noReplyEmailAddress: new FormControl(organizationRawValue.noReplyEmailAddress, {
        validators: [Validators.maxLength(255), Validators.email],
      }),
      contactNumber: new FormControl(organizationRawValue.contactNumber, {
        validators: [Validators.maxLength(255), CustomValidator.phoneNumberValidator()],
      }),
      financeManagerPIN: new FormControl(organizationRawValue.financeManagerPIN, {
        validators: [Validators.maxLength(255)],
      }),
      financeManagerSignature: new FormControl(organizationRawValue.financeManagerSignature),
      logo: new FormControl(organizationRawValue.logo),
      documentLetterHead: new FormControl(organizationRawValue.documentLetterHead),
      pfStatementLetterHead: new FormControl(organizationRawValue.pfStatementLetterHead),
      taxStatementLetterHead: new FormControl(organizationRawValue.taxStatementLetterHead),
      nomineeLetterHead: new FormControl(organizationRawValue.nomineeLetterHead),
      salaryPayslipLetterHead: new FormControl(organizationRawValue.salaryPayslipLetterHead),
      festivalBonusPayslipLetterHead: new FormControl(organizationRawValue.festivalBonusPayslipLetterHead),
      recruitmentRequisitionLetterHead: new FormControl(organizationRawValue.recruitmentRequisitionLetterHead),
      hasOrganizationStamp: new FormControl(organizationRawValue.hasOrganizationStamp),
      organizationStamp: new FormControl(organizationRawValue.organizationStamp),
      linkedin: new FormControl(organizationRawValue.linkedin, {
        validators: [Validators.maxLength(255), CustomValidator.urlValidator()],
      }),
      twitter: new FormControl(organizationRawValue.twitter, {
        validators: [Validators.maxLength(255), CustomValidator.urlValidator()],
      }),
      facebook: new FormControl(organizationRawValue.facebook, {
        validators: [Validators.maxLength(255), CustomValidator.urlValidator()],
      }),
      youtube: new FormControl(organizationRawValue.youtube, {
        validators: [Validators.maxLength(255), CustomValidator.urlValidator()],
      }),
      instagram: new FormControl(organizationRawValue.instagram, {
        validators: [Validators.maxLength(255), CustomValidator.urlValidator()],
      }),
      whatsapp: new FormControl(organizationRawValue.whatsapp, {
        validators: [Validators.maxLength(255), CustomValidator.naturalTextValidator()],
      }),
    });
  }

  getOrganization(form: OrganizationFormGroup): IOrganization | NewOrganization {
    return form.getRawValue() as IOrganization | NewOrganization;
  }

  resetForm(form: OrganizationFormGroup, organization: OrganizationFormGroupInput): void {
    const organizationRawValue = { ...this.getFormDefaults(), ...organization };
    form.reset(
      {
        ...organizationRawValue,
        id: { value: organizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrganizationFormDefaults {
    return {
      id: null,
      hasOrganizationStamp: false,
    };
  }
}
