import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import dayjs from 'dayjs/esm';

import { SalaryLockService } from './salary-lock-service';
import Swal from 'sweetalert2';
import { ISalaryGeneratorMaster } from '../../../shared/legacy/legacy-model/salary-generator-master.model';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';

@Component({
  selector: 'jhi-salary-lock',
  templateUrl: './salary-lock.component.html',
})
export class SalaryLockComponent implements OnInit {
  salaryGeneratorMasters?: ISalaryGeneratorMaster[];
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();

  allSelector = false;

  constructor(protected salaryLockService: SalaryLockService) {}

  loadAll(): void {
    this.salaryLockService
      .query()
      .subscribe((res: HttpResponse<ISalaryGeneratorMaster[]>) => (this.salaryGeneratorMasters = res.body || []));
  }

  ngOnInit(): void {
    this.approvalDTO.listOfIds = [];
    this.loadAll();
  }

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.salaryGeneratorMasters !== undefined) {
      this.salaryGeneratorMasters = this.salaryGeneratorMasters.map(d => {
        if (d.id === id) {
          d.isChecked = isChecked;
          this.allSelector = false;
          return d;
        }
        if (id === -1) {
          d.isChecked = this.allSelector;
          return d;
        }
        return d;
      });
    }
    // clear previous set
    this.selectedIdSet.clear();
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    for (let i = 0; i < this.salaryGeneratorMasters!.length; i++) {
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
      if (this.salaryGeneratorMasters![i].isChecked === true) {
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
        this.selectedIdSet.add(this.salaryGeneratorMasters![i].id);
      }
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => value as number);
  }

  toDate(year: String, month: String): string {
    return dayjs(year + '-' + month).format('MMM, YYYY');
  }

  lockSelected(): void {
    Swal.fire({
      text: 'Lock ?',
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.salaryLockService.lockSelected(this.approvalDTO).subscribe((res: HttpResponse<boolean>) => {
          if (res.body === true) {
            Swal.fire({
              text: 'Locked',
              icon: SWAL_APPROVED_ICON,
              showConfirmButton: false,
              timer: SWAL_APPROVE_REJECT_TIMER,
            });
            this.clearAllChecks();
            this.loadAll();
          } else {
            Swal.fire({
              icon: SWAL_RESPONSE_ERROR_ICON,
              text: SWAL_RESPONSE_ERROR_TEXT,
            });
          }
        });
      }
    });
  }

  unlockSelected(): void {
    Swal.fire({
      text: 'Unlock ?',
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.salaryLockService.unlockSelected(this.approvalDTO).subscribe((res: HttpResponse<boolean>) => {
          if (res.body === true) {
            Swal.fire({
              text: 'Unlocked',
              icon: SWAL_APPROVED_ICON,
              showConfirmButton: false,
              timer: SWAL_APPROVE_REJECT_TIMER,
            });
            this.clearAllChecks();
            this.loadAll();
          } else {
            Swal.fire({
              icon: SWAL_RESPONSE_ERROR_ICON,
              text: SWAL_RESPONSE_ERROR_TEXT,
            });
          }
        });
      }
    });
  }

  clearAllChecks(): void {
    this.allSelector = false;
    this.salaryGeneratorMasters?.map(salaryGeneratorMaster => {
      salaryGeneratorMaster.isChecked = false;
    });
    this.approvalDTO.listOfIds = [];
  }

  trackId(index: number, item: ISalaryGeneratorMaster): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  search(): void {}
}
