import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PfAmountDetailedReportService } from './pf-amount-detailed-report.service';
import { IDetailedPfStatement } from 'app/shared/model/pf/detailed-pf-statement.model';

@Component({
  selector: 'jhi-pf-amount-detailed-report',
  templateUrl: './pf-amount-detailed-report.component.html',
  styleUrls: ['./pf-amount-detailed-report.component.scss'],
})
export class PfAmountDetailedReportComponent implements OnInit {
  pfStatement!: IDetailedPfStatement;
  pfCode!: string;
  years: number[] = [];

  isMonthYearInvalid = false;
  disableShowButton = true;

  editForm = this.fb.group({
    startingMonth: [],
    startingYear: [],
    endingMonth: [],
    endingYear: [],
  });

  constructor(
    private fb: FormBuilder,
    private pfAccountStatementService: PfAmountDetailedReportService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const pfCode = this.activatedRoute.snapshot.params['pfCode'];
    if (pfCode != null) {
      this.pfCode = pfCode;
      this.getListOfYears(pfCode);
    }
  }

  loadDetailedPfStatement(): void {
    this.pfAccountStatementService
      .getDetailedPfStatement(this.pfCode, {
        startingYear: this.editForm.get(['startingYear'])!.value,
        startingMonth: this.editForm.get(['startingMonth'])!.value,
        endingYear: this.editForm.get(['endingYear'])!.value,
        endingMonth: this.editForm.get(['endingMonth'])!.value,
      })
      .subscribe(response => {
        this.pfStatement = response.body!;
      });
  }

  getListOfYears(pfCode: string): void {
    this.pfAccountStatementService.getListOfYears(pfCode).subscribe(res => {
      this.years = res.body || [];
      if (res.body!.length > 0) {
        const length = res.body!.length;
        this.editForm.get(['startingYear'])!.setValue(this.years[length - 1]);
        this.editForm.get(['startingMonth'])!.setValue(1);
        this.editForm.get(['endingYear'])!.setValue(this.years[0]);
        this.editForm.get(['endingMonth'])!.setValue(12);

        this.loadDetailedPfStatement();
      }
    });
  }

  monthNameLimitTo3(month: string): string {
    month = month.slice(0, 3).toLowerCase();
    month = month[0].toUpperCase() + month[1] + month[2];
    return month;
  }

  yearlyTotalContribution(totalEmployeeContributionInYear: number, totalEmployerContributionInYear: number): number {
    return totalEmployeeContributionInYear + totalEmployerContributionInYear;
  }

  previousState(): void {
    window.history.back();
  }

  public downloadAsPDF(): void {
    window.print();
  }

  onSelectMonthAndYear(): void {
    this.isMonthYearInvalid = false;
    this.disableShowButton = true;

    const startYear = this.editForm.get(['startingYear'])!.value;
    const startMonth = this.editForm.get(['startingMonth'])!.value;
    const endYear = this.editForm.get(['endingYear'])!.value;
    const endMonth = this.editForm.get(['endingMonth'])!.value;

    if (startYear > endYear) {
      this.isMonthYearInvalid = true;
    } else if (startYear === endYear) {
      if (startMonth > endMonth) {
        this.isMonthYearInvalid = true;
      } else {
        this.disableShowButton = false;
      }
    } else {
      this.disableShowButton = false;
    }
  }

  showDetailedPfStatement(): void {
    this.loadDetailedPfStatement();
  }
}
