import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatDayOfTheMonth',
})
export class FormatDayOfTheMonthPipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.format('D') : '';
  }
}
