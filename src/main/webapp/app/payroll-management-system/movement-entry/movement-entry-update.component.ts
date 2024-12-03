// import { Component, OnInit } from '@angular/core';
// import { HttpResponse } from '@angular/common/http';
// // eslint-disable-next-line @typescript-eslint/no-unused-vars
// import { FormBuilder, Validators } from '@angular/forms';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs';
// import dayjs from 'dayjs/esm';
// import {IEmployee} from "../../shared/legacy/legacy-model/employee.model";
// import {IUser} from "../../entities/user/user.model";
// import {MovementEntryService} from "../../shared/legacy/legacy-service/movement-entry.service";
// import {EmployeeService} from "../../shared/legacy/legacy-service/employee.service";
// import {UserService} from "../../entities/user/user.service";
// import {IMovementEntry, MovementEntry} from "../../shared/legacy/legacy-model/movement-entry.model";
// import {DATE_TIME_FORMAT} from "../../config/input.constants";
//
//
// type SelectableEntity = IEmployee | IUser;
//
// @Component({
//   selector: 'jhi-movement-entry-update',
//   templateUrl: './movement-entry-update.component.html',
// })
// export class MovementEntryUpdateComponent implements OnInit {
//   isSaving = false;
//   employees: IEmployee[] = [];
//   users: IUser[] = [];
//   startDateDp: any;
//   endDateDp: any;
//   createdAtDp: any;
//   updatedAtDp: any;
//   sanctionAtDp: any;
//
//   editForm = this.fb.group({
//     id: [],
//     startDate: [ ],
//     startTime: [ ],
//     startNote: [ ],
//     endDate: [ ],
//     endTime: [ ],
//     endNote: [ ],
//     type: [ ],
//     status: [ ],
//     createdAt: [ ],
//     updatedAt: [ ],
//     employeeId: [ ],
//     createdById: [ ],
//     updatedById: [ ],
//   });
//
//   constructor(
//     protected movementEntryService: MovementEntryService,
//     protected employeeService: EmployeeService,
//     protected userService: UserService,
//     protected activatedRoute: ActivatedRoute,
//     private fb: FormBuilder
//   ) {}
//
//   ngOnInit(): void {
//     this.activatedRoute.data.subscribe(({ movementEntry }) => {
//       if (!movementEntry.id) {
//         const today = dayjs().startOf('day');
//         movementEntry.startTime = today;
//         movementEntry.endTime = today;
//       }
//
//       this.updateForm(movementEntry);
//
//       this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
//
//       this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
//     });
//   }
//
//   updateForm(movementEntry: IMovementEntry): void {
//     this.editForm.patchValue({
//       id: movementEntry.id,
//       startDate: movementEntry.startDate,
//       startTime: movementEntry.startTime ? movementEntry.startTime.format(DATE_TIME_FORMAT) : null,
//       startNote: movementEntry.startNote,
//       endDate: movementEntry.endDate,
//       endTime: movementEntry.endTime ? movementEntry.endTime.format(DATE_TIME_FORMAT) : null,
//       endNote: movementEntry.endNote,
//       type: movementEntry.type,
//       status: movementEntry.status,
//       employeeId: movementEntry.employeeId,
//     });
//   }
//
//   previousState(): void {
//     window.history.back();
//   }
//
//   save(): void {
//     this.isSaving = true;
//     const movementEntry = this.createFromForm();
//     if (movementEntry.id !== undefined) {
//       this.subscribeToSaveResponse(this.movementEntryService.update(movementEntry));
//     } else {
//       this.subscribeToSaveResponse(this.movementEntryService.create(movementEntry));
//     }
//   }
//
//   private createFromForm(): IMovementEntry {
//     return {
//       ...new MovementEntry(),
//       id: this.editForm.get(['id'])!.value,
//       startDate: this.editForm.get(['startDate'])!.value,
//       startTime: this.editForm.get(['startTime'])!.value ? dayjs(this.editForm.get(['startTime'])!.value, DATE_TIME_FORMAT) : undefined,
//       startNote: this.editForm.get(['startNote'])!.value,
//       endDate: this.editForm.get(['endDate'])!.value,
//       endTime: this.editForm.get(['endTime'])!.value ? dayjs(this.editForm.get(['endTime'])!.value, DATE_TIME_FORMAT) : undefined,
//       endNote: this.editForm.get(['endNote'])!.value,
//       type: this.editForm.get(['type'])!.value,
//       status: this.editForm.get(['status'])!.value,
//       employeeId: this.editForm.get(['employeeId'])!.value,
//     };
//   }
//
//   protected subscribeToSaveResponse(result: Observable<HttpResponse<IMovementEntry>>): void {
//     result.subscribe(
//       () => this.onSaveSuccess(),
//       () => this.onSaveError()
//     );
//   }
//
//   protected onSaveSuccess(): void {
//     this.isSaving = false;
//     this.previousState();
//   }
//
//   protected onSaveError(): void {
//     this.isSaving = false;
//   }
//
//   trackById(index: number, item: SelectableEntity): any {
//     return item.id;
//   }
// }
