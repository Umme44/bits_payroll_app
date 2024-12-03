import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'jhi-select-month',
  templateUrl: './select-month.component.html',
  styleUrls: ['./select-month.component.scss'],
})
export class SelectMonthComponent implements OnInit {
  months = [
    { key: 1, value: 'January' },
    { key: 2, value: 'February' },
    { key: 3, value: 'March' },
    { key: 4, value: 'April' },
    { key: 5, value: 'May' },
    { key: 6, value: 'June' },
    { key: 7, value: 'July' },
    { key: 8, value: 'August' },
    { key: 9, value: 'September' },
    { key: 10, value: 'October' },
    { key: 11, value: 'November' },
    { key: 12, value: 'December' },
  ];

  @Input()
  ascYearOrder = true;

  @Input()
  showWithPlaceHolder = true;

  @Output() selectMonthEventEmitter = new EventEmitter<number>();

  selectMonthFormControl = new FormControl(0);

  ngOnInit(): void {
    if (this.showWithPlaceHolder) {
      this.selectMonthFormControl.setValue(null);
    } else {
      //this.selectMonthFormControl.setValue({key: this.months[0], value: 'sdfd'});
    }
  }

  onChangeMonth($event: any): void {
    this.selectMonthEventEmitter.emit(Number($event.target.value));
    // eslint-disable-next-line no-console
    console.log($event);
  }
}
