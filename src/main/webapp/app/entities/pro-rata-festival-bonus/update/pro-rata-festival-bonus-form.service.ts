// import { Injectable } from '@angular/core';
// import { FormGroup, FormControl, Validators } from '@angular/forms';
//
// import { IProRataFestivalBonus, NewProRataFestivalBonus } from '../pro-rata-festival-bonus.model';
//
// /**
//  * A partial Type with required key is used as form input.
//  */
// type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };
//
// /**
//  * Type for createFormGroup and resetForm argument.
//  * It accepts IProRataFestivalBonus for edit and NewProRataFestivalBonusFormGroupInput for create.
//  */
// type ProRataFestivalBonusFormGroupInput = IProRataFestivalBonus | PartialWithRequiredKeyOf<NewProRataFestivalBonus>;
//
// type ProRataFestivalBonusFormDefaults = Pick<NewProRataFestivalBonus, 'id'>;
//
// type ProRataFestivalBonusFormGroupContent = {
//   id: FormControl<IProRataFestivalBonus['id'] | NewProRataFestivalBonus['id']>;
//   date: FormControl<IProRataFestivalBonus['date']>;
//   amount: FormControl<IProRataFestivalBonus['amount']>;
//   description: FormControl<IProRataFestivalBonus['description']>;
//   employee: FormControl<IProRataFestivalBonus['employeeId']>;
// };
//
// export type ProRataFestivalBonusFormGroup = FormGroup<ProRataFestivalBonusFormGroupContent>;
//
// @Injectable({ providedIn: 'root' })
// export class ProRataFestivalBonusFormService {
//   createProRataFestivalBonusFormGroup(
//     proRataFestivalBonus: ProRataFestivalBonusFormGroupInput = { id: null }
//   ): ProRataFestivalBonusFormGroup {
//     const proRataFestivalBonusRawValue = {
//       ...this.getFormDefaults(),
//       ...proRataFestivalBonus,
//     };
//     return new FormGroup<ProRataFestivalBonusFormGroupContent>({
//       id: new FormControl(
//         { value: proRataFestivalBonusRawValue.id, disabled: true },
//         {
//           nonNullable: true,
//           validators: [Validators.required],
//         }
//       ),
//       date: new FormControl(proRataFestivalBonusRawValue.date),
//       amount: new FormControl(proRataFestivalBonusRawValue.amount),
//       description: new FormControl(proRataFestivalBonusRawValue.description),
//       employee: new FormControl(proRataFestivalBonusRawValue.employee),
//     });
//   }
//
//   getProRataFestivalBonus(form: ProRataFestivalBonusFormGroup): IProRataFestivalBonus | NewProRataFestivalBonus {
//     return form.getRawValue() as IProRataFestivalBonus | NewProRataFestivalBonus;
//   }
//
//   resetForm(form: ProRataFestivalBonusFormGroup, proRataFestivalBonus: ProRataFestivalBonusFormGroupInput): void {
//     const proRataFestivalBonusRawValue = { ...this.getFormDefaults(), ...proRataFestivalBonus };
//     form.reset(
//       {
//         ...proRataFestivalBonusRawValue,
//         id: { value: proRataFestivalBonusRawValue.id, disabled: true },
//       } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
//     );
//   }
//
//   private getFormDefaults(): ProRataFestivalBonusFormDefaults {
//     return {
//       id: null,
//     };
//   }
// }
