import { apiConfig } from './api-config'
import {
  AdminAnnouncementControllerApi,
  AdminCategoryControllerApi,
  AdminFeatureControllerApi,
  AdminReportControllerApi,
  AdminReviewControllerApi,
  AuthControllerApi,
} from './generated'

export const authApi = new AuthControllerApi(apiConfig)
export const adminReviewApi = new AdminReviewControllerApi(apiConfig)
export const adminCategoryApi = new AdminCategoryControllerApi(apiConfig)
export const adminReportApi = new AdminReportControllerApi(apiConfig)
export const adminAnnouncementApi = new AdminAnnouncementControllerApi(apiConfig)
export const adminFeatureApi = new AdminFeatureControllerApi(apiConfig)
