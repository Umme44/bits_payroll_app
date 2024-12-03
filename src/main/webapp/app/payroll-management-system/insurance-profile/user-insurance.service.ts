import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { IInsuranceRegistration } from '../../shared/legacy/legacy-model/insurance-registration.model';
import { IInsuranceClaim, InsuranceClaim } from '../../shared/legacy/legacy-model/insurance-claim.model';
import { IInsuranceConfiguration } from '../../shared/legacy/legacy-model/insurance-configuration.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';
import { IInsuranceRelations } from '../../shared/legacy/legacy-model/insurance-relations.model';

type RegistrationEntityResponseType = HttpResponse<IInsuranceRegistration>;
// type RelationEntityResponseType = HttpResponse<IInsuranceClaim>;
type ClaimEntityResponseType = HttpResponse<InsuranceClaim>;
// type ClaimReportResponseType = HttpResponse<IInsuranceClaimReport>;
type ClaimEntityArrayResponseType = HttpResponse<InsuranceClaim[]>;
type RegistrationEntityArrayResponseType = HttpResponse<IInsuranceRegistration[]>;
// type RelationEntityArrayResponseType = HttpResponse<IInsuranceRelation[]>;
type ConfigurationEntityResponseType = HttpResponse<IInsuranceConfiguration>;
// type ConfigurationEntityResponseArrayType = HttpResponse<IInsuranceConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class UserInsuranceService {
  public resourceUrlRegistrationCommon = SERVER_API_URL + 'api/common/insurance-registrations';
  public resourceUrlClaimCommon = SERVER_API_URL + 'api/common/insurance-claims';
  public resourceUrlClaimReportCommon = SERVER_API_URL + 'api/common/insurance-claim-report';
  public resourceUrlClaimReportAdmin = SERVER_API_URL + 'api/employee-mgt/insurance-claim-report';
  public resourceUrlConfigurationCommon = SERVER_API_URL + 'api/common/insurance-configurations';
  public resourceUrlSelfDetails = SERVER_API_URL + 'api/common/insurance-registrations/self-details';

  constructor(protected http: HttpClient) {}

  createInsuranceRegistration(insuranceRegistration: IInsuranceRegistration): Observable<RegistrationEntityResponseType> {
    const copy = this.convertDateFromClient(insuranceRegistration);
    return this.http
      .post<IInsuranceRegistration>(this.resourceUrlRegistrationCommon, copy, { observe: 'response' })
      .pipe(map((res: RegistrationEntityResponseType) => this.convertDateFromServer(res)));
  }

  createInsuranceRegistrationMultipart(
    file: File,
    insuranceRegistration: IInsuranceRegistration
  ): Observable<RegistrationEntityResponseType> {
    const formData: FormData = new FormData();
    const copy = this.convertDateFromClient(insuranceRegistration);
    formData.append('file', file);
    formData.append('insuranceRegistration', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .post<IInsuranceRegistration>(this.resourceUrlRegistrationCommon + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: RegistrationEntityResponseType) => this.convertDateFromServer(res)));
  }

  updateInsuranceRegistrationMultipart(
    file: File,
    insuranceRegistration: IInsuranceRegistration
  ): Observable<RegistrationEntityResponseType> {
    const formData: FormData = new FormData();
    const copy = this.convertDateFromClient(insuranceRegistration);
    formData.append('file', file);
    formData.append('insuranceRegistration', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<IInsuranceRegistration>(this.resourceUrlRegistrationCommon + '/multipart', formData, { observe: 'response' })
      .pipe(map((res: RegistrationEntityResponseType) => this.convertDateFromServer(res)));
  }

  updateInsuranceRegistration(insuranceRegistration: IInsuranceRegistration): Observable<RegistrationEntityResponseType> {
    const copy = this.convertDateFromClient(insuranceRegistration);
    return this.http
      .put<IInsuranceRegistration>(this.resourceUrlRegistrationCommon, copy, { observe: 'response' })
      .pipe(map((res: RegistrationEntityResponseType) => this.convertDateFromServer(res)));
  }

  findInsuranceRegistration(id: number): Observable<RegistrationEntityResponseType> {
    return this.http
      .get<IInsuranceRegistration>(`${this.resourceUrlRegistrationCommon}/${id}`, { observe: 'response' })
      .pipe(map((res: RegistrationEntityResponseType) => this.convertDateFromServer(res)));
  }

  findSelfDetailsForInsuranceRegistration(): Observable<HttpResponse<IEmployee>> {
    return this.http
      .get<IEmployee>(`${this.resourceUrlSelfDetails}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<IEmployee>) => this.convertDateFromServerForSelfDetails(res)));
  }

  convertDateFromServerForSelfDetails(res: HttpResponse<IEmployee>): HttpResponse<IEmployee> {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
    }
    return res;
  }

  queryAllInsuranceRegistrations(req?: any): Observable<RegistrationEntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInsuranceRegistration[]>(this.resourceUrlRegistrationCommon, { params: options, observe: 'response' })
      .pipe(map((res: RegistrationEntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryInsuranceClaim(req?: any): Observable<ClaimEntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInsuranceClaim[]>(this.resourceUrlClaimCommon, { params: options, observe: 'response' })
      .pipe(map((res: ClaimEntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findInsuranceClaimById(id: number): Observable<ClaimEntityResponseType> {
    return this.http
      .get<IInsuranceClaim>(`${this.resourceUrlClaimCommon}/${id}`, { observe: 'response' })
      .pipe(map((res: ClaimEntityResponseType) => this.convertDateFromServerForClaim(res)));
  }

  queryRemainingInsuranceRelations(req?: any): Observable<HttpResponse<IInsuranceRelations>> {
    const options = createRequestOption(req);
    return this.http.get<IInsuranceRelations>(`${this.resourceUrlRegistrationCommon}/remaining-relations`, {
      params: options,
      observe: 'response',
    });
  }

  queryAllInsuranceRelations(req?: any): Observable<HttpResponse<IInsuranceRelations>> {
    const options = createRequestOption(req);
    return this.http.get<IInsuranceRelations>(`${this.resourceUrlRegistrationCommon}/all-relations`, {
      params: options,
      observe: 'response',
    });
  }

  queryInsuranceConfiguration(req?: any): Observable<ConfigurationEntityResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInsuranceConfiguration>(this.resourceUrlConfigurationCommon, { params: options, observe: 'response' });
  }

  deleteInsuranceRegistration(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrlRegistrationCommon}/${id}`, { observe: 'response' });
  }

  deleteInsuranceClaim(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrlClaimCommon}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(insuranceRegistration: IInsuranceRegistration): IInsuranceRegistration {
    const copy: IInsuranceRegistration = Object.assign({}, insuranceRegistration, {
      dateOfBirth:
        insuranceRegistration.dateOfBirth && insuranceRegistration.dateOfBirth.isValid()
          ? insuranceRegistration.dateOfBirth.format(DATE_FORMAT)
          : undefined,
      approvedAt:
        insuranceRegistration.approvedAt && insuranceRegistration.approvedAt.isValid()
          ? insuranceRegistration.approvedAt.toJSON()
          : undefined,
      createdAt:
        insuranceRegistration.createdAt && insuranceRegistration.createdAt.isValid() ? insuranceRegistration.createdAt.toJSON() : undefined,
      updatedAt:
        insuranceRegistration.updatedAt && insuranceRegistration.updatedAt.isValid() ? insuranceRegistration.updatedAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: RegistrationEntityResponseType): RegistrationEntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
      res.body.approvedAt = res.body.approvedAt ? dayjs(res.body.approvedAt) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateFromServerForClaim(res: ClaimEntityResponseType): ClaimEntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: RegistrationEntityArrayResponseType): RegistrationEntityArrayResponseType {
    if (res.body) {
      res.body.forEach((insuranceRegistration: IInsuranceRegistration) => {
        insuranceRegistration.dateOfBirth = insuranceRegistration.dateOfBirth ? dayjs(insuranceRegistration.dateOfBirth) : undefined;
        insuranceRegistration.approvedAt = insuranceRegistration.approvedAt ? dayjs(insuranceRegistration.approvedAt) : undefined;
        insuranceRegistration.createdAt = insuranceRegistration.createdAt ? dayjs(insuranceRegistration.createdAt) : undefined;
        insuranceRegistration.updatedAt = insuranceRegistration.updatedAt ? dayjs(insuranceRegistration.updatedAt) : undefined;
      });
    }
    return res;
  }
}
