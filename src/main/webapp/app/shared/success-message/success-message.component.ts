import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-success-message',
  templateUrl: './success-message.component.html',
  styleUrls: ['./success-message.component.scss'],
})
export class SuccessMessageComponent implements OnInit {
  @Input()
  text!: string;
  @Output() passEntry: EventEmitter<boolean> = new EventEmitter();

  constructor(private activeModal: NgbActiveModal) {}

  ngOnInit(): void {}

  confirm(): void {
    this.passEntry.emit(true);
    this.activeModal.dismiss();
  }
}
