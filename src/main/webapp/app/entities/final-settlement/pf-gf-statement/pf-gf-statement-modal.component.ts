import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IPfGfStatement } from '../../shared/model/pf-gf/pf-gf-statement.model';
import { FinalSettlementService } from './final-settlement.service';

@Component({
  selector: 'jhi-pf-gf-statement-modal',
  templateUrl: './pf-gf-statement-modal.component.html',
  styleUrls: ['./pf-gf-statement-modal.component.scss'],
})
export class PfGfStatementModalComponent implements OnInit {
  pfGfStatement!: IPfGfStatement;
  todayDate = new Date();

  @Input()
  employeeId!: number;

  @Output() passEntry: EventEmitter<boolean> = new EventEmitter();

  @ViewChild('pdfPfGfStatement', { static: false })
  pdfPfGfStatement: ElementRef | undefined;

  organizationFullName = '';

  constructor(private activeModal: NgbActiveModal, private finalSettlementService: FinalSettlementService) {
    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {
    this.finalSettlementService.getPfGfStatement(this.employeeId).subscribe(response => {
      this.pfGfStatement = response;
    });
  }

  cancel(): void {
    this.passEntry.emit(false);
    this.activeModal.dismiss();
  }

  public downloadAsPDF(): void {
    window.print();
  }
}
