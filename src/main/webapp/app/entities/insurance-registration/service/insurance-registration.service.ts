import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInsuranceRegistration, NewInsuranceRegistration } from '../insurance-registration.model';
import { IInsuranceRelations } from '../../../shared/legacy/legacy-model/insurance-relations.model';
import { IEmployeeMinimal } from '../../../shared/model/employee-minimal.model';
import { IEmployee } from '../../employee/employee.model';
import { IInsuranceRegistrationAdmin } from '../insurance-registration-admin-model';
import { IInsuranceApprovalDto } from '../insurance-approval.model';

export type PartialUpdateInsuranceRegistration = Partial<IInsuranceRegistration> & Pick<IInsuranceRegistration, 'id'>;

type RestOf<T extends IInsuranceRegistration | NewInsuranceRegistration> = Omit<
  T,
  'dateOfBirth' | 'updatedAt' | 'approvedAt' | 'createdAt'
> & {
  dateOfBirth?: string | null;
  updatedAt?: string | null;
  approvedAt?: string | null;
  createdAt?: string | null;
};

export type RestInsuranceRegistration = RestOf<IInsuranceRegistration>;

export type NewRestInsuranceRegistration = RestOf<NewInsuranceRegistration>;

export type PartialUpdateRestInsuranceRegistration = RestOf<PartialUpdateInsuranceRegistration>;

export type EntityResponseType = HttpResponse<IInsuranceRegistration>;
export type EntityArrayResponseType = HttpResponse<IInsuranceRegistration[]>;

@Injectable({ providedIn: 'root' })
export class InsuranceRegistrationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/insurance-registrations');
  public resourceUrlForEmployeeDetails = SERVER_API_URL + 'api/employee-mgt/insurance-registrations/employee';
  public resourceUrlApproveInsuranceRegistration = SERVER_API_URL + 'api/employee-mgt/insurance-registrations/approve';
  public resourceUrlRejectInsuranceRegistration = SERVER_API_URL + 'api/employee-mgt/insurance-registrations/reject';
  public resourceUrlCancelInsuranceRegistration = SERVER_API_URL + 'api/employee-mgt/insurance-registrations/cancel';
  public resourceUrlForExportData = SERVER_API_URL + 'api/employee-mgt/export-insurance-registration';

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(insuranceRegistration: NewInsuranceRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(insuranceRegistration);
    return this.http
      .post<RestInsuranceRegistration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  createInsuranceRegistrationAdminMultipart(file: File, insuranceRegistration: IInsuranceRegistration): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    const copy = this.convertDateFromClient(insuranceRegistration);
    formData.append('file', file);
    formData.append('insuranceRegistration', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .post<RestInsuranceRegistration>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  updateInsuranceRegistrationMultipart(file: File, insuranceRegistration: IInsuranceRegistration): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    const copy = this.convertDateFromClient(insuranceRegistration);
    formData.append('file', file);
    formData.append('insuranceRegistration', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<RestInsuranceRegistration>(this.resourceUrl + '/multipart', formData, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  updateInsuranceRegistration(insuranceRegistration: IInsuranceRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(insuranceRegistration);
    return this.http
      .put<RestInsuranceRegistration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(insuranceRegistration: IInsuranceRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(insuranceRegistration);
    return this.http
      .put<RestInsuranceRegistration>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(insuranceRegistration: PartialUpdateInsuranceRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(insuranceRegistration);
    return this.http
      .patch<RestInsuranceRegistration>(`${this.resourceUrl}/${this.getInsuranceRegistrationIdentifier(insuranceRegistration)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInsuranceRegistration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findInsuranceRegistrationByInsuranceCardId(cardId: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestInsuranceRegistration[]>(`${this.resourceUrl}/find-by-insurance-card-id/${cardId}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInsuranceRegistration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getAllInsuranceRegistrations(req?: any): Observable<HttpResponse<IInsuranceRegistrationAdmin[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<IInsuranceRegistrationAdmin[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: HttpResponse<IInsuranceRegistrationAdmin[]>) => this.convertDateArrayFromServerForAdmin(res)));
  }

  getEligibleEmployees(): Observable<HttpResponse<IEmployeeMinimal[]>> {
    return this.http.get<IEmployeeMinimal[]>(this.resourceUrl + '/eligible-employees', { observe: 'response' });
  }

  queryAllInsuranceRelations(): Observable<HttpResponse<IInsuranceRelations>> {
    return this.http.get<IInsuranceRelations>(this.resourceUrl + `/all-relations`, { observe: 'response' });
  }

  queryEmployeeDetails(id: number): Observable<HttpResponse<IEmployeeMinimal>> {
    return this.http
      .get<IEmployeeMinimal>(this.resourceUrlForEmployeeDetails + `/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<IEmployeeMinimal>) => this.convertDateFromServerForEmployeeDetails(res)));
  }

  queryRemainingInsuranceRelationByEmployeeId(employeeId: number): Observable<HttpResponse<IInsuranceRelations>> {
    return this.http.get<IInsuranceRelations>(this.resourceUrl + `/${employeeId}/remaining-relations`, { observe: 'response' });
  }

  queryPreviousRegistrationsByEmployeeId(employeeId: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestInsuranceRegistration[]>(this.resourceUrl + `/${employeeId}/previous-registrations`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  isCardIdUnique(insuranceCardId: String, req?: any): Observable<HttpResponse<boolean>> {
    const options = createRequestOption(req);
    return this.http.get<boolean>(this.resourceUrl + `/${insuranceCardId}/is-unique`, {
      params: options,
      observe: 'response',
    });
  }

  approveInsuranceRegistration(insuranceApprovalDto: IInsuranceApprovalDto): Observable<EntityResponseType> {
    const copy = Object.assign({}, insuranceApprovalDto);
    return this.http
      .post<RestInsuranceRegistration>(this.resourceUrlApproveInsuranceRegistration, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  rejectInsuranceRegistration(insuranceApprovalDto: IInsuranceApprovalDto): Observable<EntityResponseType> {
    const copy = Object.assign({}, insuranceApprovalDto);
    return this.http
      .post<RestInsuranceRegistration>(this.resourceUrlRejectInsuranceRegistration, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  cancelInsuranceRegistration(insuranceApprovalDto: IInsuranceApprovalDto): Observable<EntityResponseType> {
    const copy = Object.assign({}, insuranceApprovalDto);
    return this.http
      .post<RestInsuranceRegistration>(this.resourceUrlCancelInsuranceRegistration, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  exportInclusionList(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrlForExportData + '/inclusion-list', { params: options, responseType: 'blob' });
  }

  exportApprovedList(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrlForExportData + '/approved-list', { params: options, responseType: 'blob' });
  }

  exportExcludedList(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrlForExportData + '/excluded-list', { params: options, responseType: 'blob' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInsuranceRegistrationIdentifier(insuranceRegistration: Pick<IInsuranceRegistration, 'id'>): number {
    return insuranceRegistration.id;
  }

  compareInsuranceRegistration(o1: Pick<IInsuranceRegistration, 'id'> | null, o2: Pick<IInsuranceRegistration, 'id'> | null): boolean {
    return o1 && o2 ? this.getInsuranceRegistrationIdentifier(o1) === this.getInsuranceRegistrationIdentifier(o2) : o1 === o2;
  }

  addInsuranceRegistrationToCollectionIfMissing<Type extends Pick<IInsuranceRegistration, 'id'>>(
    insuranceRegistrationCollection: Type[],
    ...insuranceRegistrationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const insuranceRegistrations: Type[] = insuranceRegistrationsToCheck.filter(isPresent);
    if (insuranceRegistrations.length > 0) {
      const insuranceRegistrationCollectionIdentifiers = insuranceRegistrationCollection.map(
        insuranceRegistrationItem => this.getInsuranceRegistrationIdentifier(insuranceRegistrationItem)!
      );
      const insuranceRegistrationsToAdd = insuranceRegistrations.filter(insuranceRegistrationItem => {
        const insuranceRegistrationIdentifier = this.getInsuranceRegistrationIdentifier(insuranceRegistrationItem);
        if (insuranceRegistrationCollectionIdentifiers.includes(insuranceRegistrationIdentifier)) {
          return false;
        }
        insuranceRegistrationCollectionIdentifiers.push(insuranceRegistrationIdentifier);
        return true;
      });
      return [...insuranceRegistrationsToAdd, ...insuranceRegistrationCollection];
    }
    return insuranceRegistrationCollection;
  }

  protected convertDateFromClient<T extends IInsuranceRegistration | NewInsuranceRegistration | PartialUpdateInsuranceRegistration>(
    insuranceRegistration: T
  ): RestOf<T> {
    return {
      ...insuranceRegistration,
      dateOfBirth: insuranceRegistration.dateOfBirth?.format(DATE_FORMAT) ?? null,
      updatedAt: insuranceRegistration.updatedAt?.toJSON() ?? null,
      approvedAt: insuranceRegistration.approvedAt?.toJSON() ?? null,
      createdAt: insuranceRegistration.createdAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restInsuranceRegistration: RestInsuranceRegistration): IInsuranceRegistration {
    return {
      ...restInsuranceRegistration,
      dateOfBirth: restInsuranceRegistration.dateOfBirth ? dayjs(restInsuranceRegistration.dateOfBirth) : undefined,
      updatedAt: restInsuranceRegistration.updatedAt ? dayjs(restInsuranceRegistration.updatedAt) : undefined,
      approvedAt: restInsuranceRegistration.approvedAt ? dayjs(restInsuranceRegistration.approvedAt) : undefined,
      createdAt: restInsuranceRegistration.createdAt ? dayjs(restInsuranceRegistration.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInsuranceRegistration>): HttpResponse<IInsuranceRegistration> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInsuranceRegistration[]>): HttpResponse<IInsuranceRegistration[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  protected convertDateFromServerForEmployeeDetails(res: HttpResponse<IEmployeeMinimal>): HttpResponse<IEmployeeMinimal> {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServerForAdmin(
    res: HttpResponse<IInsuranceRegistrationAdmin[]>
  ): HttpResponse<IInsuranceRegistrationAdmin[]> {
    if (res.body) {
      res.body.forEach((insuranceRegistrationAdmin: IInsuranceRegistrationAdmin) => {
        insuranceRegistrationAdmin.insuranceRegistrationDTOList = this.convertDateFromServerForAdmin(
          insuranceRegistrationAdmin.insuranceRegistrationDTOList!
        );
      });
    }
    return res;
  }

  protected convertDateFromServerForAdmin(insuranceRegistrationList: IInsuranceRegistration[]): IInsuranceRegistration[] {
    if (insuranceRegistrationList) {
      insuranceRegistrationList.forEach((insuranceRegistration: IInsuranceRegistration) => {
        insuranceRegistration.dateOfBirth = insuranceRegistration.dateOfBirth ? dayjs(insuranceRegistration.dateOfBirth) : undefined;
        insuranceRegistration.approvedAt = insuranceRegistration.approvedAt ? dayjs(insuranceRegistration.approvedAt) : undefined;
        insuranceRegistration.createdAt = insuranceRegistration.createdAt ? dayjs(insuranceRegistration.createdAt) : undefined;
        insuranceRegistration.updatedAt = insuranceRegistration.updatedAt ? dayjs(insuranceRegistration.updatedAt) : undefined;
      });
    }
    return insuranceRegistrationList;
  }
}
