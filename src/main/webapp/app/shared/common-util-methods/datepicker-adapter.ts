// /**
//  * Angular bootstrap Date adapter
//  */
// import {Injectable} from '@angular/core';
// import {NgbDateAdapter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
// import dayjs from 'dayjs/esm';
//
// @Injectable()
// export class NgbDateMomentAdapter extends NgbDateAdapter<dayjs.Dayjs> {
//
//   fromModel(date: dayjs.Dayjs): NgbDateStruct {
//     if (date && dayjs.isDayjs(date) && date.isValid()) {
//       return {year: date.year(), month: date.month() + 1, day: date.date()};
//     }
//     // ! can be removed after https://github.com/ng-bootstrap/ng-bootstrap/issues/1544 is resolved
//     return null!;
//   }
//
//   toModel(date: NgbDateStruct): dayjs.Dayjs {
//     // ! after null can be removed after https://github.com/ng-bootstrap/ng-bootstrap/issues/1544 is resolved
//     return date ? dayjs(date.year + '-' + date.month + '-' + date.day, 'YYYY-MM-DD') : null!;
//   }
//
// }
