import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-tax-calculation',
  templateUrl: './tax-calculation.component.html',
  styles: [
    `
      ::ng-deep table {
        border: none !important;
        width: 100% !important;
      }

      ::ng-deep tr > td:first-child {
        text-align: left;
      }

      ::ng-deep tr > td:not(:first-child) {
        text-align: right;
      }

      ::ng-deep tr > th:first-child {
        text-align: left;
      }

      ::ng-deep tr > th:not(:first-child) {
        text-align: right;
      }
    `,
  ],
})
export class TaxCalculationComponent implements OnInit {
  @Input()
  text!: string;

  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit(): void {}

  close(): void {
    this.activeModal.dismiss();
  }
}
