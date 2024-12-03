import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalaryDeduction } from '../salary-deduction.model';
import { IDeductionType } from '../../deduction-type/deduction-type.model';
import { DeductionTypeService } from '../../deduction-type/service/deduction-type.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-salary-deduction-detail',
  templateUrl: './salary-deduction-detail.component.html',
})
export class SalaryDeductionDetailComponent implements OnInit {
  salaryDeduction: ISalaryDeduction | null = null;
  deductionTypes!: IDeductionType[];

  constructor(protected activatedRoute: ActivatedRoute, protected deductionTypeService: DeductionTypeService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryDeduction }) => (this.salaryDeduction = salaryDeduction));
    this.deductionTypeService.query().subscribe((res: HttpResponse<IDeductionType[]>) => (this.deductionTypes = res.body || []));
  }

  toDate(year: number, month: number): Date {
    return new Date(Number(year), Number(month) - 1);
  }

  getDeductionType(deductionTypeId: number): string {
    const deductionType = this.deductionTypes.find(x => x.id === deductionTypeId);
    return deductionType!.name!;
  }

  previousState(): void {
    window.history.back();
  }
}
