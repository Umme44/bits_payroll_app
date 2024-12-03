import { Pipe, PipeTransform } from '@angular/core';

import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatTime',
})
export class FormatTimePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.format('hh:mm A') : '';
  }
}
