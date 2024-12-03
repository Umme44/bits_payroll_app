import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MyTeamService } from './my-team.service';
import { IMyTeamEmployee } from '../../shared/model/my-team-employee.model';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-my-team',
  templateUrl: './my-team.component.html',
  styleUrls: ['./my-team.component.scss'],
})
export class MyTeamComponent implements OnInit {
  myTeam: IMyTeamEmployee[] | null = [];
  myTeamFiltered: IMyTeamEmployee[] | null = [];
  searchText = new FormControl('');

  constructor(private myTeamService: MyTeamService, private router: Router) {}

  ngOnInit(): void {
    this.myTeamService.query().subscribe(res => {
      this.myTeam = res.body;
      this.myTeamFiltered = this.myTeam;
    });
  }

  public getStringValue(status: any): string {
    if (status === 'WEEKLY_OFFDAY') return 'OFFDAY';
    if (status === 'NON_FULFILLED_OFFICE_HOURS') return 'NFOH';
    if (status === 'GOVT_HOLIDAY') return ' GOVT HOLIDAY';
    return status;
  }

  public checkNFOH(status: any): boolean {
    return status === 'NON_FULFILLED_OFFICE_HOURS';
  }

  public convertMinsToHrsMins(workinghour: any): string {
    if (workinghour === null || workinghour === undefined) return '00:00';
    const h = Math.floor(workinghour);
    let m = workinghour - h;
    m = Math.floor(m * 100);
    const hour = h < 10 ? '0' + h : h + '';
    const minute = m < 10 ? '0' + m : m + '';

    return Number(hour) > 0 ? hour + ' hour ' + minute + ' minutes' : minute + ' minutes';
  }

  search(): void {
    this.myTeamFiltered = this.myTeam!.filter(attendance => {
      if (
        attendance.employee?.pin?.toString().match(this.searchText.value) ||
        attendance.employee?.fullName?.toLowerCase().match(this.searchText.value.toString().toLowerCase())
      ) {
        return attendance;
      }
      return null;
    });
  }

  navigateMyTeamAts(id: number): void {
    const defaultSelectedMemberId = this.myTeamFiltered![0].employee!.id;
    sessionStorage.setItem('defaultSelectedMemberId', JSON.stringify(defaultSelectedMemberId));
    sessionStorage.setItem('selectedTeamMemberId', JSON.stringify(id));
    this.router.navigate(['my-team/attendance-time-sheet']);
  }

  normalizeEmployeeName(fullName: any): string {
    const employeeName = fullName.toString();
    //check name is greater than 35 characters long
    if (employeeName.length > 35) {
      let abbreviateForm = '';
      const names = employeeName.split(' ');
      const excludingLastPart = names.slice(0, names.length - 1);
      excludingLastPart.forEach((x: string) => {
        abbreviateForm = abbreviateForm + x.charAt(0) + '. ';
      });
      return abbreviateForm + names[names.length - 1];
    }
    return fullName;
  }

  onSearchTextChangeV2(searchText: any): void {
    this.searchText.setValue(searchText);
    this.search();
  }
}
