import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { Filter } from '../../common/employee-address-book/filter.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';
import { ApplicationConfigService } from '../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IEmployee>;
type EntityArrayResponseType = HttpResponse<IEmployee[]>;

@Injectable({ providedIn: 'root' })
export class UpcomingEventProbationEndService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(filter: Filter, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<IEmployee[]>(`${this.resourceUrl}/employee-mgt/probation-end-employees`, filter, {
        params: options,
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(employee: IEmployee): IEmployee {
    const copy: IEmployee = Object.assign({}, employee, {
      dateOfBirth: employee.dateOfBirth && employee.dateOfBirth.isValid() ? employee.dateOfBirth.format(DATE_FORMAT) : undefined,
      dateOfMarriage:
        employee.dateOfMarriage && employee.dateOfMarriage.isValid() ? employee.dateOfMarriage.format(DATE_FORMAT) : undefined,
      dateOfJoining: employee.dateOfJoining && employee.dateOfJoining.isValid() ? employee.dateOfJoining.format(DATE_FORMAT) : undefined,
      dateOfConfirmation:
        employee.dateOfConfirmation && employee.dateOfConfirmation.isValid() ? employee.dateOfConfirmation.format(DATE_FORMAT) : undefined,
      probationPeriodExtendedTo:
        employee.probationPeriodExtendedTo && employee.probationPeriodExtendedTo.isValid()
          ? employee.probationPeriodExtendedTo.format(DATE_FORMAT)
          : undefined,
      passportIssuedDate:
        employee.passportIssuedDate && employee.passportIssuedDate.isValid() ? employee.passportIssuedDate.format(DATE_FORMAT) : undefined,
      passportExpiryDate:
        employee.passportExpiryDate && employee.passportExpiryDate.isValid() ? employee.passportExpiryDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employee: IEmployee) => {
        employee.dateOfBirth = employee.dateOfBirth ? dayjs(employee.dateOfBirth) : undefined;
        employee.dateOfMarriage = employee.dateOfMarriage ? dayjs(employee.dateOfMarriage) : undefined;
        employee.dateOfJoining = employee.dateOfJoining ? dayjs(employee.dateOfJoining) : undefined;
        employee.dateOfConfirmation = employee.dateOfConfirmation ? dayjs(employee.dateOfConfirmation) : undefined;
        employee.probationPeriodExtendedTo = employee.probationPeriodExtendedTo ? dayjs(employee.probationPeriodExtendedTo) : undefined;
        employee.passportIssuedDate = employee.passportIssuedDate ? dayjs(employee.passportIssuedDate) : undefined;
        employee.passportExpiryDate = employee.passportExpiryDate ? dayjs(employee.passportExpiryDate) : undefined;
        employee.probationPeriodEndDate = employee.probationPeriodEndDate ? dayjs(employee.probationPeriodEndDate) : undefined;
        employee.contractPeriodExtendedTo = employee.contractPeriodExtendedTo ? dayjs(employee.contractPeriodExtendedTo) : undefined;
        employee.contractPeriodEndDate = employee.contractPeriodEndDate ? dayjs(employee.contractPeriodEndDate) : undefined;
        employee.allowance01EffectiveFrom = employee.allowance01EffectiveFrom ? dayjs(employee.allowance01EffectiveFrom) : undefined;
        employee.allowance01EffectiveTo = employee.allowance01EffectiveTo ? dayjs(employee.allowance01EffectiveTo) : undefined;
        employee.allowance02EffectiveFrom = employee.allowance02EffectiveFrom ? dayjs(employee.allowance02EffectiveFrom) : undefined;
        employee.allowance02EffectiveTo = employee.allowance02EffectiveTo ? dayjs(employee.allowance02EffectiveTo) : undefined;
        employee.allowance03EffectiveFrom = employee.allowance03EffectiveFrom ? dayjs(employee.allowance03EffectiveFrom) : undefined;
        employee.allowance03EffectiveTo = employee.allowance03EffectiveTo ? dayjs(employee.allowance03EffectiveTo) : undefined;
        employee.allowance04EffectiveFrom = employee.allowance04EffectiveFrom ? dayjs(employee.allowance04EffectiveFrom) : undefined;
        employee.allowance04EffectiveTo = employee.allowance04EffectiveTo ? dayjs(employee.allowance04EffectiveTo) : undefined;
        employee.allowance05EffectiveFrom = employee.allowance05EffectiveFrom ? dayjs(employee.allowance05EffectiveFrom) : undefined;
        employee.allowance05EffectiveTo = employee.allowance05EffectiveTo ? dayjs(employee.allowance05EffectiveTo) : undefined;
        employee.allowance06EffectiveFrom = employee.allowance06EffectiveFrom ? dayjs(employee.allowance06EffectiveFrom) : undefined;
        employee.allowance06EffectiveTo = employee.allowance06EffectiveTo ? dayjs(employee.allowance06EffectiveTo) : undefined;
        employee.createdAt = employee.createdAt ? dayjs(employee.createdAt) : undefined;
        employee.updatedAt = employee.updatedAt ? dayjs(employee.updatedAt) : undefined;
        employee.currentInTime = employee.currentInTime ? dayjs(employee.currentInTime) : undefined;
        employee.currentOutTime = employee.currentOutTime ? dayjs(employee.currentOutTime) : undefined;
        employee.onlineAttendanceSanctionedAt = employee.onlineAttendanceSanctionedAt
          ? dayjs(employee.onlineAttendanceSanctionedAt)
          : undefined;
      });
    }
    return res;
  }
}
