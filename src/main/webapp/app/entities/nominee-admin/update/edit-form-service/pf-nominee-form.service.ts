import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IPfNominee, NewPfNominee } from '../../pf-nominee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPfNominee for edit and NewPfNomineeFormGroupInput for create.
 */
type PfNomineeFormGroupInput = IPfNominee | PartialWithRequiredKeyOf<NewPfNominee>;

type PfNomineeFormDefaults = Pick<NewPfNominee, 'id' | 'isNidVerified' | 'isGuardianNidVerified' | 'isApproved'>;

type PfNomineeFormGroupContent = {
  id: FormControl<IPfNominee['id'] | NewPfNominee['id']>;
  nominationDate: FormControl<IPfNominee['nominationDate']>;
  fullName: FormControl<IPfNominee['fullName']>;
  presentAddress: FormControl<IPfNominee['presentAddress']>;
  permanentAddress: FormControl<IPfNominee['permanentAddress']>;
  relationship: FormControl<IPfNominee['relationship']>;
  dateOfBirth: FormControl<IPfNominee['dateOfBirth']>;
  age: FormControl<IPfNominee['age']>;
  sharePercentage: FormControl<IPfNominee['sharePercentage']>;
  nidNumber: FormControl<IPfNominee['nidNumber']>;
  isNidVerified: FormControl<IPfNominee['isNidVerified']>;
  passportNumber: FormControl<IPfNominee['passportNumber']>;
  brnNumber: FormControl<IPfNominee['brnNumber']>;
  photo: FormControl<IPfNominee['photo']>;
  guardianName: FormControl<IPfNominee['guardianName']>;
  guardianFatherOrSpouseName: FormControl<IPfNominee['guardianFatherOrSpouseName']>;
  guardianDateOfBirth: FormControl<IPfNominee['guardianDateOfBirth']>;
  guardianPresentAddress: FormControl<IPfNominee['guardianPresentAddress']>;
  guardianPermanentAddress: FormControl<IPfNominee['guardianPermanentAddress']>;
  guardianProofOfIdentityOfLegalGuardian: FormControl<IPfNominee['guardianProofOfIdentityOfLegalGuardian']>;
  guardianRelationshipWithNominee: FormControl<IPfNominee['guardianRelationshipWithNominee']>;
  guardianNidNumber: FormControl<IPfNominee['guardianNidNumber']>;
  guardianBrnNumber: FormControl<IPfNominee['guardianBrnNumber']>;
  guardianIdNumber: FormControl<IPfNominee['guardianIdNumber']>;
  isGuardianNidVerified: FormControl<IPfNominee['isGuardianNidVerified']>;
  isApproved: FormControl<IPfNominee['isApproved']>;
  identityType: FormControl<IPfNominee['identityType']>;
  idNumber: FormControl<IPfNominee['idNumber']>;
  documentName: FormControl<IPfNominee['documentName']>;
  guardianIdentityType: FormControl<IPfNominee['guardianIdentityType']>;
  guardianDocumentName: FormControl<IPfNominee['guardianDocumentName']>;
  pfAccountId: FormControl<IPfNominee['pfAccountId']>;
  pfWitnessId: FormControl<IPfNominee['pfWitnessId']>;
  approvedById: FormControl<IPfNominee['approvedById']>;
};

export type PfNomineeFormGroup = FormGroup<PfNomineeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PfNomineeFormService {
  createPfNomineeFormGroup(pfNominee: PfNomineeFormGroupInput = { id: null }): PfNomineeFormGroup {
    const pfNomineeRawValue = {
      ...this.getFormDefaults(),
      ...pfNominee,
    };
    return new FormGroup<PfNomineeFormGroupContent>({
      id: new FormControl(
        { value: pfNomineeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nominationDate: new FormControl(pfNomineeRawValue.nominationDate),
      fullName: new FormControl(pfNomineeRawValue.fullName),
      presentAddress: new FormControl(pfNomineeRawValue.presentAddress),
      permanentAddress: new FormControl(pfNomineeRawValue.permanentAddress),
      relationship: new FormControl(pfNomineeRawValue.relationship),
      dateOfBirth: new FormControl(pfNomineeRawValue.dateOfBirth),
      age: new FormControl(pfNomineeRawValue.age),
      sharePercentage: new FormControl(pfNomineeRawValue.sharePercentage),
      nidNumber: new FormControl(pfNomineeRawValue.nidNumber),
      isNidVerified: new FormControl(pfNomineeRawValue.isNidVerified),
      passportNumber: new FormControl(pfNomineeRawValue.passportNumber),
      brnNumber: new FormControl(pfNomineeRawValue.brnNumber),
      photo: new FormControl(pfNomineeRawValue.photo),
      guardianName: new FormControl(pfNomineeRawValue.guardianName),
      guardianFatherOrSpouseName: new FormControl(pfNomineeRawValue.guardianFatherOrSpouseName),
      guardianDateOfBirth: new FormControl(pfNomineeRawValue.guardianDateOfBirth),
      guardianPresentAddress: new FormControl(pfNomineeRawValue.guardianPresentAddress),
      guardianPermanentAddress: new FormControl(pfNomineeRawValue.guardianPermanentAddress),
      guardianProofOfIdentityOfLegalGuardian: new FormControl(pfNomineeRawValue.guardianProofOfIdentityOfLegalGuardian),
      guardianRelationshipWithNominee: new FormControl(pfNomineeRawValue.guardianRelationshipWithNominee),
      guardianNidNumber: new FormControl(pfNomineeRawValue.guardianNidNumber),
      guardianBrnNumber: new FormControl(pfNomineeRawValue.guardianBrnNumber),
      guardianIdNumber: new FormControl(pfNomineeRawValue.guardianIdNumber),
      isGuardianNidVerified: new FormControl(pfNomineeRawValue.isGuardianNidVerified),
      isApproved: new FormControl(pfNomineeRawValue.isApproved),
      identityType: new FormControl(pfNomineeRawValue.identityType, {
        validators: [Validators.required],
      }),
      idNumber: new FormControl(pfNomineeRawValue.idNumber, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(50)],
      }),
      documentName: new FormControl(pfNomineeRawValue.documentName, {
        validators: [Validators.minLength(0), Validators.maxLength(200)],
      }),
      guardianIdentityType: new FormControl(pfNomineeRawValue.guardianIdentityType),
      guardianDocumentName: new FormControl(pfNomineeRawValue.guardianDocumentName, {
        validators: [Validators.minLength(0), Validators.maxLength(200)],
      }),
      pfAccountId: new FormControl(pfNomineeRawValue.pfAccountId),
      pfWitnessId: new FormControl(pfNomineeRawValue.pfWitnessId),
      approvedById: new FormControl(pfNomineeRawValue.approvedById),
    });
  }

  getPfNominee(form: PfNomineeFormGroup): IPfNominee | NewPfNominee {
    return form.getRawValue() as IPfNominee | NewPfNominee;
  }

  resetForm(form: PfNomineeFormGroup, pfNominee: PfNomineeFormGroupInput): void {
    const pfNomineeRawValue = { ...this.getFormDefaults(), ...pfNominee };
    form.reset(
      {
        ...pfNomineeRawValue,
        id: { value: pfNomineeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PfNomineeFormDefaults {
    return {
      id: null,
      isNidVerified: false,
      isGuardianNidVerified: false,
      isApproved: false,
    };
  }
}
