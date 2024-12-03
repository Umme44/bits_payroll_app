import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IPfStatement } from '../../../shared/model/pf/pf-statement.model';
import { FinalSettlementService } from '../service/final-settlement.service';

@Component({
  selector: 'jhi-pf-statement-modal',
  templateUrl: './pf-statement.component.html',
  styleUrls: ['./pf-statement.component.scss'],
})
export class PfStatementComponent implements OnInit {
  pfStatement!: IPfStatement;

  constructor(private finalSettlementService: FinalSettlementService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    const employeeId = this.activatedRoute.snapshot.params['employeeId'];
    if (employeeId != null) {
      this.finalSettlementService.getPfStatement(employeeId).subscribe(response => {
        this.pfStatement = response;
      });
    }
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
}
