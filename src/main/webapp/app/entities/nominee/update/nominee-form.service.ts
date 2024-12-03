import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INominee, NewNominee } from '../nominee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INominee for edit and NewNomineeFormGroupInput for create.
 */
type NomineeFormGroupInput = INominee | PartialWithRequiredKeyOf<NewNominee>;

type NomineeFormDefaults = Pick<NewNominee, 'id' | 'isLocked' | 'isNidVerified' | 'isGuardianNidVerified'>;

type NomineeFormGroupContent = {
  id: FormControl<INominee['id'] | NewNominee['id']>;
  nomineeName: FormControl<INominee['nomineeName']>;
  presentAddress: FormControl<INominee['presentAddress']>;
  relationshipWithEmployee: FormControl<INominee['relationshipWithEmployee']>;
  dateOfBirth: FormControl<INominee['dateOfBirth']>;
  age: FormControl<INominee['age']>;
  sharePercentage: FormControl<INominee['sharePercentage']>;
  imagePath: FormControl<INominee['imagePath']>;
  status: FormControl<INominee['status']>;
  guardianName: FormControl<INominee['guardianName']>;
  guardianFatherName: FormControl<INominee['guardianFatherName']>;
  guardianSpouseName: FormControl<INominee['guardianSpouseName']>;
  guardianDateOfBirth: FormControl<INominee['guardianDateOfBirth']>;
  guardianPresentAddress: FormControl<INominee['guardianPresentAddress']>;
  guardianDocumentName: FormControl<INominee['guardianDocumentName']>;
  guardianRelationshipWith: FormControl<INominee['guardianRelationshipWith']>;
  guardianImagePath: FormControl<INominee['guardianImagePath']>;
  isLocked: FormControl<INominee['isLocked']>;
  nominationDate: FormControl<INominee['nominationDate']>;
  permanentAddress: FormControl<INominee['permanentAddress']>;
  guardianPermanentAddress: FormControl<INominee['guardianPermanentAddress']>;
  nomineeType: FormControl<INominee['nomineeType']>;
  identityType: FormControl<INominee['identityType']>;
  documentName: FormControl<INominee['documentName']>;
  idNumber: FormControl<INominee['idNumber']>;
  isNidVerified: FormControl<INominee['isNidVerified']>;
  guardianIdentityType: FormControl<INominee['guardianIdentityType']>;
  guardianIdNumber: FormControl<INominee['guardianIdNumber']>;
  isGuardianNidVerified: FormControl<INominee['isGuardianNidVerified']>;
  employee: FormControl<INominee['employee']>;
  approvedBy: FormControl<INominee['approvedBy']>;
  witness: FormControl<INominee['witness']>;
  member: FormControl<INominee['member']>;
};

export type NomineeFormGroup = FormGroup<NomineeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NomineeFormService {
  createNomineeFormGroup(nominee: NomineeFormGroupInput = { id: null }): NomineeFormGroup {
    const nomineeRawValue = {
      ...this.getFormDefaults(),
      ...nominee,
    };
    return new FormGroup<NomineeFormGroupContent>({
      id: new FormControl(
        { value: nomineeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomineeName: new FormControl(nomineeRawValue.nomineeName, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(255)],
      }),
      presentAddress: new FormControl(nomineeRawValue.presentAddress, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(255)],
      }),
      relationshipWithEmployee: new FormControl(nomineeRawValue.relationshipWithEmployee),
      dateOfBirth: new FormControl(nomineeRawValue.dateOfBirth),
      age: new FormControl(nomineeRawValue.age),
      sharePercentage: new FormControl(nomineeRawValue.sharePercentage, {
        validators: [Validators.required, Validators.min(1), Validators.max(100)],
      }),
      imagePath: new FormControl(nomineeRawValue.imagePath),
      status: new FormControl(nomineeRawValue.status, {
        validators: [Validators.required],
      }),
      guardianName: new FormControl(nomineeRawValue.guardianName, {
        validators: [Validators.minLength(0), Validators.maxLength(255)],
      }),
      guardianFatherName: new FormControl(nomineeRawValue.guardianFatherName),
      guardianSpouseName: new FormControl(nomineeRawValue.guardianSpouseName),
      guardianDateOfBirth: new FormControl(nomineeRawValue.guardianDateOfBirth),
      guardianPresentAddress: new FormControl(nomineeRawValue.guardianPresentAddress, {
        validators: [Validators.minLength(0), Validators.maxLength(255)],
      }),
      guardianDocumentName: new FormControl(nomineeRawValue.guardianDocumentName),
      guardianRelationshipWith: new FormControl(nomineeRawValue.guardianRelationshipWith),
      guardianImagePath: new FormControl(nomineeRawValue.guardianImagePath),
      isLocked: new FormControl(nomineeRawValue.isLocked),
      nominationDate: new FormControl(nomineeRawValue.nominationDate),
      permanentAddress: new FormControl(nomineeRawValue.permanentAddress, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(255)],
      }),
      guardianPermanentAddress: new FormControl(nomineeRawValue.guardianPermanentAddress, {
        validators: [Validators.minLength(0), Validators.maxLength(255)],
      }),
      nomineeType: new FormControl(nomineeRawValue.nomineeType),
      identityType: new FormControl(nomineeRawValue.identityType),
      documentName: new FormControl(nomineeRawValue.documentName),
      idNumber: new FormControl(nomineeRawValue.idNumber),
      isNidVerified: new FormControl(nomineeRawValue.isNidVerified),
      guardianIdentityType: new FormControl(nomineeRawValue.guardianIdentityType),
      guardianIdNumber: new FormControl(nomineeRawValue.guardianIdNumber),
      isGuardianNidVerified: new FormControl(nomineeRawValue.isGuardianNidVerified),
      employee: new FormControl(nomineeRawValue.employee),
      approvedBy: new FormControl(nomineeRawValue.approvedBy),
      witness: new FormControl(nomineeRawValue.witness),
      member: new FormControl(nomineeRawValue.member),
    });
  }

  getNominee(form: NomineeFormGroup): INominee | NewNominee {
    return form.getRawValue() as INominee | NewNominee;
  }

  resetForm(form: NomineeFormGroup, nominee: NomineeFormGroupInput): void {
    const nomineeRawValue = { ...this.getFormDefaults(), ...nominee };
    form.reset(
      {
        ...nomineeRawValue,
        id: { value: nomineeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): NomineeFormDefaults {
    return {
      id: null,
      isLocked: false,
      isNidVerified: false,
      isGuardianNidVerified: false,
    };
  }
}
