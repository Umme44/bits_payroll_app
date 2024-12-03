import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IUserEditAccount } from './user-edit-account.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { DATE_FORMAT } from '../../config/input.constants';
import dayjs from 'dayjs/esm';

type EntityResponseType = HttpResponse<IEmployee>;
type EntityArrayResponseType = HttpResponse<IEmployee[]>;

@Injectable({ providedIn: 'root' })
export class UserEditAccountInformationService {
  public resourceUrl = SERVER_API_URL + 'api/common/edit-account';

  constructor(protected http: HttpClient) {}

  update(employee: IUserEditAccount): Observable<HttpResponse<IUserEditAccount>> {
    const copy = this.convertDateFromClient(employee);
    return this.http
      .put<IUserEditAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(): Observable<EntityResponseType> {
    return this.http
      .get<IEmployee>(`${this.resourceUrl}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  uploadDP(file: File): Observable<any> {
    const url = `${this.resourceUrl}/photo`;

    const formData = new FormData();
    formData.append('photo', file);

    return this.http.post(url, formData, { observe: 'response' });
  }

  syncImage(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/sync`, { observe: 'response' });
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
      probationPeriodEndDate:
        employee.probationPeriodEndDate && employee.probationPeriodEndDate.isValid()
          ? employee.probationPeriodEndDate.format(DATE_FORMAT)
          : undefined,
      contractPeriodExtendedTo:
        employee.contractPeriodExtendedTo && employee.contractPeriodExtendedTo.isValid()
          ? employee.contractPeriodExtendedTo.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
      res.body.dateOfMarriage = res.body.dateOfMarriage ? dayjs(res.body.dateOfMarriage) : undefined;
      res.body.dateOfJoining = res.body.dateOfJoining ? dayjs(res.body.dateOfJoining) : undefined;
      res.body.dateOfConfirmation = res.body.dateOfConfirmation ? dayjs(res.body.dateOfConfirmation) : undefined;
      res.body.probationPeriodExtendedTo = res.body.probationPeriodExtendedTo ? dayjs(res.body.probationPeriodExtendedTo) : undefined;
      res.body.passportIssuedDate = res.body.passportIssuedDate ? dayjs(res.body.passportIssuedDate) : undefined;
      res.body.passportExpiryDate = res.body.passportExpiryDate ? dayjs(res.body.passportExpiryDate) : undefined;
      res.body.probationPeriodEndDate = res.body.probationPeriodEndDate ? dayjs(res.body.probationPeriodEndDate) : undefined;
      res.body.contractPeriodExtendedTo = res.body.contractPeriodExtendedTo ? dayjs(res.body.contractPeriodExtendedTo) : undefined;
    }
    return res;
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
      });
    }
    return res;
  }
}
