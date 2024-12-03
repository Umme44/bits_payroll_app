import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatYear',
})
export class FormatYearPipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.format('YYYY') : '';
  }
}
